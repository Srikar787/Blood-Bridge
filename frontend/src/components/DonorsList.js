import React, { useState, useEffect, useCallback } from 'react';
import { Link, Navigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import './DonorsList.css';

const DonorsList = () => {
  const [donors, setDonors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { token, user } = useAuth();

  const fetchDonors = useCallback(async () => {
    try {
      setLoading(true);
      const config = {};
      if (token) {
        config.headers = {
          Authorization: `Bearer ${token}`
        };
      }
      const response = await axios.get('http://localhost:8081/api/donors', config);
      setDonors(response.data);
      setError(null);
    } catch (err) {
      console.error('Error fetching donors:', err);
      if (err.response?.status === 401) {
        setError('Please log in as admin to view donors.');
      } else if (err.response?.status === 403) {
        setError('You do not have permission to view donors.');
      } else if (err.code === 'ECONNREFUSED') {
        setError('Failed to connect to backend. Make sure the backend is running on port 8081.');
      } else {
        setError(err.response?.data?.message || 'Failed to fetch donors. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  }, [token]);

  useEffect(() => {
    fetchDonors();
  }, [fetchDonors]);

  // Only admins should access donors list
  if (user && user.role !== 'ADMIN') {
    return <Navigate to="/" replace />;
  }

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this donor?')) {
      try {
        await axios.delete(`http://localhost:8081/api/donors/${id}`);
        fetchDonors();
      } catch (err) {
        alert('Failed to delete donor');
        console.error('Error deleting donor:', err);
      }
    }
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
        <p>Loading donors...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error-container">
        <div className="error-icon">⚠️</div>
        <h2>Error Loading Donors</h2>
        <p>{error}</p>
        <button onClick={fetchDonors} className="btn btn-primary">
          Try Again
        </button>
      </div>
    );
  }

  return (
    <div className="donors-list-page">
      <div className="page-header">
        <div>
          <h1>Blood Donors</h1>
          <p>View and manage registered blood donors</p>
        </div>
        <Link to="/add-donor" className="btn btn-primary">
          + Add New Donor
        </Link>
      </div>

      {donors.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">🩸</div>
          <h2>No Donors Yet</h2>
          <p>Be the first to register as a blood donor!</p>
          <Link to="/add-donor" className="btn btn-primary">
            Register Now
          </Link>
        </div>
      ) : (
        <>
          <div className="stats-bar">
            <div className="stat-item">
              <span className="stat-value">{donors.length}</span>
              <span className="stat-label">Total Donors</span>
            </div>
            <div className="stat-item">
              <span className="stat-value">{donors.filter(d => d.isActive).length}</span>
              <span className="stat-label">Active</span>
            </div>
            <div className="stat-item">
              <span className="stat-value">{donors.filter(d => d.isEligible).length}</span>
              <span className="stat-label">Eligible</span>
            </div>
          </div>

          <div className="donors-grid">
            {donors.map((donor) => (
              <div key={donor.id} className="donor-card">
                <div className="donor-card-header">
                  <div className="donor-avatar-large">
                    {donor.name.charAt(0).toUpperCase()}
                  </div>
                  <div className="donor-title">
                    <h3>{donor.name}</h3>
                    <span className={`blood-badge-large ${donor.bloodType?.replace('+', 'plus').replace('-', 'minus')}`}>
                      {donor.bloodType}
                    </span>
                  </div>
                </div>

                <div className="donor-details-list">
                  <div className="detail-row">
                    <span className="detail-label">📧 Email</span>
                    <span className="detail-value">{donor.email}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">📞 Phone</span>
                    <span className="detail-value">{donor.phone}</span>
                  </div>
                  {donor.address && (
                    <div className="detail-row">
                      <span className="detail-label">📍 Address</span>
                      <span className="detail-value">{donor.address}</span>
                    </div>
                  )}
                  {donor.age && (
                    <div className="detail-row">
                      <span className="detail-label">👤 Age</span>
                      <span className="detail-value">{donor.age} years</span>
                    </div>
                  )}
                  {donor.gender && (
                    <div className="detail-row">
                      <span className="detail-label">⚧ Gender</span>
                      <span className="detail-value">{donor.gender}</span>
                    </div>
                  )}
                  {donor.lastDonationDate && (
                    <div className="detail-row">
                      <span className="detail-label">📅 Last Donation</span>
                      <span className="detail-value">
                        {new Date(donor.lastDonationDate).toLocaleDateString()}
                      </span>
                    </div>
                  )}
                </div>

                <div className="donor-status">
                  <span className={`status-badge ${donor.isActive ? 'active' : 'inactive'}`}>
                    {donor.isActive ? '✓ Active' : '✗ Inactive'}
                  </span>
                  {donor.isEligible && (
                    <span className="status-badge eligible">✓ Eligible</span>
                  )}
                </div>

                <div className="donor-actions">
                  <a href={`tel:${donor.phone}`} className="btn btn-primary btn-sm">
                    📞 Call
                  </a>
                  <a href={`mailto:${donor.email}`} className="btn btn-outline btn-sm">
                    ✉️ Email
                  </a>
                  <button
                    className="btn btn-ghost btn-sm btn-danger"
                    onClick={() => handleDelete(donor.id)}
                  >
                    🗑️ Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default DonorsList;
