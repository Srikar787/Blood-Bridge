import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import NotificationModal from './NotificationModal';
import './RequestorDashboard.css';

const RequestorDashboard = () => {
  const [searchData, setSearchData] = useState({
    bloodType: '',
    latitude: null,
    longitude: null,
    address: '',
    radiusKm: 10
  });
  const [donors, setDonors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [eligibilityCriteria, setEligibilityCriteria] = useState(null);
  const [locationSuggestions, setLocationSuggestions] = useState([]);
  const [locationLoading, setLocationLoading] = useState(false);
  const [selectedDonor, setSelectedDonor] = useState(null);
  const [showNotificationModal, setShowNotificationModal] = useState(false);
  const locationDebounceRef = useRef(null);
  const { user, token } = useAuth();

  useEffect(() => {
    fetchEligibilityCriteria();
  }, []);

  const fetchEligibilityCriteria = async () => {
    try {
      const response = await axios.get('http://localhost:8081/api/donors/search/eligibility-criteria');
      setEligibilityCriteria(response.data);
    } catch (err) {
      console.error('Error fetching eligibility criteria:', err);
    }
  };

  const getCurrentLocation = () => {
    if (!navigator.geolocation) {
      setError('Geolocation is not supported by your browser');
      return;
    }

    setLoading(true);
    navigator.geolocation.getCurrentPosition(
      (position) => {
        setSearchData(prev => ({
          ...prev,
          latitude: position.coords.latitude,
          longitude: position.coords.longitude
        }));
        setError('');
        setLoading(false);
      },
      (err) => {
        setError('Unable to retrieve your location. Please enter manually.');
        setLoading(false);
      }
    );
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setSearchData(prev => ({ ...prev, [name]: value }));
    setError('');

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
    setSearchData((prev) => ({
      ...prev,
      address: suggestion.label,
      latitude: suggestion.lat,
      longitude: suggestion.lon,
    }));
    setLocationSuggestions([]);
    setError('');
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    
    if (!searchData.bloodType) {
      setError('Please select a blood type');
      return;
    }

    if (!searchData.latitude || !searchData.longitude) {
      setError('Please provide location (use GPS or enter address)');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await axios.post(
        'http://localhost:8081/api/donors/search/nearby',
        {
          bloodType: searchData.bloodType,
          latitude: searchData.latitude,
          longitude: searchData.longitude,
          radiusKm: searchData.radiusKm
        }
      );
      setDonors(response.data.donors || []);
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to search donors');
    } finally {
      setLoading(false);
    }
  };

  const calculateDistance = (lat1, lon1, lat2, lon2) => {
    const R = 6371; // Earth's radius in km
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
              Math.sin(dLon/2) * Math.sin(dLon/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return (R * c).toFixed(1);
  };

  return (
    <div className="requestor-dashboard">
      <div className="dashboard-header">
        <h1>Find Blood Donors</h1>
        <p>Search for eligible donors near you</p>
      </div>

      <div className="dashboard-content">
        <div className="search-section">
          <div className="search-card">
            <h2>Search Criteria</h2>
            
            {eligibilityCriteria && (
              <div className="eligibility-info">
                <h3>Eligibility Requirements</h3>
                <ul>
                  <li>Age: {eligibilityCriteria.minAge} - {eligibilityCriteria.maxAge} years</li>
                  <li>Minimum {eligibilityCriteria.minDaysSinceLastDonation} days since last donation</li>
                </ul>
              </div>
            )}

            <form onSubmit={handleSearch} className="search-form">
              <div className="form-group">
                <label htmlFor="bloodType">Blood Type Required *</label>
                <select
                  id="bloodType"
                  name="bloodType"
                  className="input"
                  value={searchData.bloodType}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select blood type</option>
                  <option value="A+">A+</option>
                  <option value="A-">A-</option>
                  <option value="B+">B+</option>
                  <option value="B-">B-</option>
                  <option value="AB+">AB+</option>
                  <option value="AB-">AB-</option>
                  <option value="O+">O+</option>
                  <option value="O-">O-</option>
                </select>
              </div>

              <div className="form-group">
                <label>Location</label>
                <div className="location-input-group">
                  <input
                    type="text"
                    name="address"
                    className="input"
                    value={searchData.address}
                    onChange={handleChange}
                    placeholder="Enter address or city"
                  />
                  <button
                    type="button"
                    onClick={getCurrentLocation}
                    className="btn btn-outline btn-gps"
                    disabled={loading}
                  >
                    📍 Use GPS
                  </button>
                </div>
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
                {searchData.latitude && searchData.longitude && (
                  <p className="location-confirmed">
                    ✓ Location set: {searchData.latitude.toFixed(4)}, {searchData.longitude.toFixed(4)}
                  </p>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="radiusKm">Search Radius: {searchData.radiusKm} km</label>
                <input
                  type="range"
                  id="radiusKm"
                  name="radiusKm"
                  min="5"
                  max="20"
                  value={searchData.radiusKm}
                  onChange={handleChange}
                  className="range-input"
                />
                <div className="range-labels">
                  <span>5 km</span>
                  <span>20 km</span>
                </div>
              </div>

              {error && <div className="alert alert-error">{error}</div>}

              <button type="submit" className="btn btn-primary btn-full" disabled={loading}>
                {loading ? 'Searching...' : '🔍 Search Donors'}
              </button>
            </form>
          </div>
        </div>

        <div className="results-section">
          <div className="results-header">
            <h2>Available Donors</h2>
            {donors.length > 0 && (
              <span className="results-count">{donors.length} donor{donors.length !== 1 ? 's' : ''} found</span>
            )}
          </div>

          {donors.length === 0 && !loading && (
            <div className="empty-state">
              <div className="empty-icon">🔍</div>
              <p>No donors found. Try adjusting your search criteria.</p>
            </div>
          )}

          <div className="donors-grid">
            {donors.map((donor) => {
              const distance = searchData.latitude && searchData.longitude
                ? calculateDistance(searchData.latitude, searchData.longitude, donor.latitude, donor.longitude)
                : 'N/A';

              return (
                <div key={donor.id} className="donor-result-card">
                  <div className="donor-header">
                    <div className="donor-avatar">{donor.name.charAt(0)}</div>
                    <div className="donor-info">
                      <h3>{donor.name}</h3>
                      <span className="blood-badge">{donor.bloodType}</span>
                    </div>
                  </div>
                  
                  <div className="donor-details">
                    <div className="detail-item">
                      <span className="detail-icon">📧</span>
                      <span>{donor.email}</span>
                    </div>
                    <div className="detail-item">
                      <span className="detail-icon">📞</span>
                      <span>{donor.phone}</span>
                    </div>
                    {donor.address && (
                      <div className="detail-item">
                        <span className="detail-icon">📍</span>
                        <span>{donor.address}</span>
                      </div>
                    )}
                    <div className="detail-item">
                      <span className="detail-icon">📏</span>
                      <span>{distance} km away</span>
                    </div>
                    {donor.age && (
                      <div className="detail-item">
                        <span className="detail-icon">👤</span>
                        <span>{donor.age} years, {donor.gender}</span>
                      </div>
                    )}
                  </div>

                  <div className="donor-actions">
                    <button
                      className="btn btn-primary btn-sm"
                      onClick={() => {
                        setSelectedDonor(donor);
                        setShowNotificationModal(true);
                      }}
                    >
                      🔔 Send Notification
                    </button>
                    <a href={`tel:${donor.phone}`} className="btn btn-outline btn-sm">
                      📞 Call
                    </a>
                    <a href={`mailto:${donor.email}`} className="btn btn-outline btn-sm">
                      ✉️ Email
                    </a>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      {selectedDonor && (
        <NotificationModal
          donor={selectedDonor}
          isOpen={showNotificationModal}
          onClose={() => {
            setShowNotificationModal(false);
            setSelectedDonor(null);
          }}
          onSuccess={() => {
            // Optionally show success message or refresh
          }}
        />
      )}
    </div>
  );
};

export default RequestorDashboard;

