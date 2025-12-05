import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import './AddDonor.css';

const AddDonor = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    userId: user?.email || '',
    name: '',
    email: '',
    phone: '',
    bloodType: '',
    address: '',
    latitude: null,
    longitude: null,
    gender: '',
    age: '',
    lastDonationDate: '',
    isActive: true
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [eligibilityStatus, setEligibilityStatus] = useState(null);
  const [successDonor, setSuccessDonor] = useState(null);
  const [locationSuggestions, setLocationSuggestions] = useState([]);
  const [locationLoading, setLocationLoading] = useState(false);
  const locationDebounceRef = useRef(null);

  const getCurrentLocation = () => {
    if (!navigator.geolocation) {
      setError('Geolocation is not supported by your browser');
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        setFormData({
          ...formData,
          latitude: position.coords.latitude,
          longitude: position.coords.longitude
        });
        setError(null);
      },
      (err) => {
        setError('Unable to retrieve your location. Please enter address manually.');
      }
    );
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    setError(null);

    // Trigger location suggestions when typing address
    if (name === 'address') {
      if (locationDebounceRef.current) {
        clearTimeout(locationDebounceRef.current);
      }
      locationDebounceRef.current = setTimeout(() => {
        fetchLocationSuggestions(value);
      }, 350);
    }
  };

  const fetchLocationSuggestions = async (query) => {
    if (!query || query.length < 3) {
      setLocationSuggestions([]);
      return;
    }
    try {
      setLocationLoading(true);
      const res = await fetch(
        `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}&limit=5`
      );
      const data = await res.json();
      setLocationSuggestions(
        data.map((item) => ({
          label: item.display_name,
          lat: parseFloat(item.lat),
          lon: parseFloat(item.lon),
        }))
      );
    } catch (err) {
      console.error('Location lookup failed', err);
    } finally {
      setLocationLoading(false);
    }
  };

  const handleSelectLocation = (suggestion) => {
    setFormData((prev) => ({
      ...prev,
      address: suggestion.label,
      latitude: suggestion.lat,
      longitude: suggestion.lon,
    }));
    setLocationSuggestions([]);
    setError(null);
  };

  const checkEligibility = () => {
    const age = parseInt(formData.age);
    const isAgeValid = age >= 18 && age <= 65;
    const hasRequiredFields = formData.name && formData.email && formData.phone &&
      formData.bloodType && formData.gender &&
      formData.latitude && formData.longitude;

    if (!isAgeValid) {
      setEligibilityStatus({ eligible: false, message: 'Age must be between 18 and 65 years' });
      return;
    }

    if (!hasRequiredFields) {
      setEligibilityStatus({ eligible: false, message: 'Please fill all required fields including location' });
      return;
    }

    setEligibilityStatus({ eligible: true, message: 'You are eligible to donate blood!' });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccessDonor(null);

    // Convert age to integer
    const donorData = {
      ...formData,
      age: formData.age ? parseInt(formData.age) : null,
      latitude: formData.latitude ? parseFloat(formData.latitude) : null,
      longitude: formData.longitude ? parseFloat(formData.longitude) : null
    };

    try {
      const res = await axios.post('http://localhost:8081/api/donors', donorData);
      setSuccessDonor(res.data);
      // Optionally keep the form filled so user can see what was submitted
      // Or clear fields below if preferred
      // setFormData({ ...formData, name:'', email:'', phone:'', address:'', latitude:null, longitude:null, age:'', gender:'', bloodType:'', lastDonationDate:'', isActive:true });
    } catch (err) {
      let errorMessage = 'Failed to add donor. ';
      if (err.response) {
        errorMessage += `Server error: ${err.response.status} - ${err.response.data?.message || err.response.statusText}`;
      } else if (err.request) {
        errorMessage += 'Backend is not responding. Please ensure the backend is running on http://localhost:8081';
      } else {
        errorMessage += err.message || 'Unknown error occurred';
      }
      setError(errorMessage);
      console.error('Error adding donor:', err);
    } finally {
      setLoading(false);
    }
  };

  const bloodTypes = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

  return (
    <div className="add-donor-page">
      <div className="donor-form-container">
        <div className="form-header">
          <h1>Register as Blood Donor</h1>
          <p>Join our community and help save lives</p>
        </div>

        <form onSubmit={handleSubmit} className="donor-form">
          {error && <div className="alert alert-error">{error}</div>}
          {successDonor && (
            <div className="alert alert-success">
              <strong>Registration successful!</strong>
              <div className="success-details">
                <div><strong>Name:</strong> {successDonor.name}</div>
                <div><strong>Email:</strong> {successDonor.email}</div>
                <div><strong>Phone:</strong> {successDonor.phone}</div>
                <div><strong>Blood Type:</strong> {successDonor.bloodType}</div>
                {successDonor.address && <div><strong>Address:</strong> {successDonor.address}</div>}
                {successDonor.latitude && successDonor.longitude && (
                  <div><strong>Location:</strong> {successDonor.latitude.toFixed(4)}, {successDonor.longitude.toFixed(4)}</div>
                )}
                {successDonor.isEligible !== undefined && (
                  <div><strong>Eligibility:</strong> {successDonor.isEligible ? 'Eligible' : 'Not eligible (auto-checked)'}</div>
                )}
              </div>
            </div>
          )}

          <div className="form-section">
            <h3 className="section-title">Personal Information</h3>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="name">Full Name *</label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  className="input"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  placeholder="John Doe"
                />
              </div>

              <div className="form-group">
                <label htmlFor="email">Email *</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  className="input"
                  value={formData.email}
                  onChange={handleChange}
                  required
                  placeholder="john@example.com"
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="phone">Phone Number *</label>
                <input
                  type="tel"
                  id="phone"
                  name="phone"
                  className="input"
                  value={formData.phone}
                  onChange={handleChange}
                  required
                  placeholder="+1 234 567 8900"
                />
              </div>

              <div className="form-group">
                <label htmlFor="age">Age *</label>
                <input
                  type="number"
                  id="age"
                  name="age"
                  className="input"
                  value={formData.age}
                  onChange={handleChange}
                  min="18"
                  max="65"
                  required
                  placeholder="25"
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="gender">Gender *</label>
                <select
                  id="gender"
                  name="gender"
                  className="input"
                  value={formData.gender}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select gender</option>
                  <option value="M">Male</option>
                  <option value="F">Female</option>
                  <option value="Other">Other</option>
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="bloodType">Blood Type *</label>
                <select
                  id="bloodType"
                  name="bloodType"
                  className="input"
                  value={formData.bloodType}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select blood type</option>
                  {bloodTypes.map(type => (
                    <option key={type} value={type}>{type}</option>
                  ))}
                </select>
              </div>
            </div>
          </div>

          <div className="form-section">
            <h3 className="section-title">Location</h3>

            <div className="form-group">
              <label htmlFor="address">Address *</label>
              <input
                type="text"
                id="address"
                name="address"
                className="input"
                value={formData.address}
                onChange={handleChange}
                required
                placeholder="123 Main St, City, State, ZIP"
              />
              {locationLoading && <p className="hint">Searching locations...</p>}
              {locationSuggestions.length > 0 && (
                <div className="suggestions">
                  {locationSuggestions.map((s, idx) => (
                    <button
                      type="button"
                      key={idx}
                      className="suggestion-item"
                      onClick={() => handleSelectLocation(s)}
                    >
                      {s.label}
                    </button>
                  ))}
                </div>
              )}
            </div>

            <div className="location-group">
              <div className="form-group">
                <label htmlFor="latitude">Latitude</label>
                <input
                  type="number"
                  id="latitude"
                  name="latitude"
                  className="input"
                  value={formData.latitude || ''}
                  onChange={handleChange}
                  step="any"
                  placeholder="12.9716"
                />
              </div>

              <div className="form-group">
                <label htmlFor="longitude">Longitude</label>
                <input
                  type="number"
                  id="longitude"
                  name="longitude"
                  className="input"
                  value={formData.longitude || ''}
                  onChange={handleChange}
                  step="any"
                  placeholder="77.5946"
                />
              </div>

              <button
                type="button"
                onClick={getCurrentLocation}
                className="btn btn-outline btn-gps"
              >
                📍 Use GPS
              </button>
            </div>

            {formData.latitude != null && formData.longitude != null && (
              <p className="location-confirmed">
                ✓ Location set: {Number(formData.latitude).toFixed(4)}, {Number(formData.longitude).toFixed(4)}
              </p>
            )}

          </div>

          <div className="form-section">
            <h3 className="section-title">Donation History</h3>

            <div className="form-group">
              <label htmlFor="lastDonationDate">Last Donation Date</label>
              <input
                type="date"
                id="lastDonationDate"
                name="lastDonationDate"
                className="input"
                value={formData.lastDonationDate}
                onChange={handleChange}
                max={new Date().toISOString().split('T')[0]}
              />
              <small className="form-hint">Must be at least 56 days since last donation</small>
            </div>

            <div className="form-group checkbox-group">
              <label htmlFor="isActive">
                <input
                  type="checkbox"
                  id="isActive"
                  name="isActive"
                  checked={formData.isActive}
                  onChange={handleChange}
                />
                <span>I am currently available to donate</span>
              </label>
            </div>
          </div>

          <div className="eligibility-check">
            <button type="button" onClick={checkEligibility} className="btn btn-secondary">
              Check Eligibility
            </button>
            {eligibilityStatus && (
              <div className={`eligibility-status ${eligibilityStatus.eligible ? 'eligible' : 'not-eligible'}`}>
                {eligibilityStatus.message}
              </div>
            )}
          </div>

          <div className="form-actions">
            <button type="submit" className="btn btn-primary btn-large" disabled={loading}>
              {loading ? 'Registering...' : 'Register as Donor'}
            </button>
            <button type="button" className="btn btn-ghost" onClick={() => navigate('/donors')}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddDonor;
