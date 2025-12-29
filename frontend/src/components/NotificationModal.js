import React, { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import { API_ENDPOINTS } from '../config';
import './NotificationModal.css';

const NotificationModal = ({ donor, isOpen, onClose, onSuccess }) => {
  const { user, token } = useAuth();
  const [formData, setFormData] = useState({
    message: '',
    urgency: 'MEDIUM',
    requestorName: user?.name || '',
    requestorPhone: user?.phone || ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess(false);

    try {
      const config = {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      };

      const requestData = {
        donorId: donor.id,
        message: formData.message || `Hello ${donor.name}, I need ${donor.bloodType} blood. Please contact me if you can help.`,
        urgency: formData.urgency,
        bloodType: donor.bloodType,
        requestorName: formData.requestorName,
        requestorPhone: formData.requestorPhone
      };

      const response = await axios.post(
        API_ENDPOINTS.NOTIFICATIONS.SEND,
        requestData,
        config
      );

      // Check for SMS warnings
      if (response.data.smsWarning) {
        console.warn('SMS Warning:', response.data.smsWarning);
        // Show warning but still show success
        setError(response.data.smsWarning.join ? response.data.smsWarning.join(', ') : response.data.smsWarning);
      }

      setSuccess(true);
      setTimeout(() => {
        onSuccess && onSuccess();
        onClose();
        setFormData({
          message: '',
          urgency: 'MEDIUM',
          requestorName: user?.name || '',
          requestorPhone: user?.phone || ''
        });
        setSuccess(false);
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to send notification. Please try again.');
      console.error('Error sending notification:', err);
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Send Notification to {donor.name}</h2>
          <button className="modal-close" onClick={onClose}>×</button>
        </div>

        <div className="modal-body">
          {success && (
            <div className="alert alert-success">
              ✓ Notification sent successfully! The donor will be notified.
            </div>
          )}

          {error && (
            <div className="alert alert-error">
              {error}
            </div>
          )}

          <div className="donor-info-card">
            <div className="donor-info-item">
              <span className="label">Blood Type:</span>
              <span className="value blood-badge">{donor.bloodType}</span>
            </div>
            <div className="donor-info-item">
              <span className="label">Email:</span>
              <span className="value">{donor.email}</span>
            </div>
            <div className="donor-info-item">
              <span className="label">Phone:</span>
              <span className="value">{donor.phone}</span>
            </div>
          </div>

          <form onSubmit={handleSubmit} className="notification-form">
            <div className="form-group">
              <label htmlFor="requestorName">Your Name *</label>
              <input
                type="text"
                id="requestorName"
                name="requestorName"
                className="input"
                value={formData.requestorName}
                onChange={handleChange}
                required
                placeholder="Enter your name"
              />
            </div>

            <div className="form-group">
              <label htmlFor="requestorPhone">Your Phone Number *</label>
              <input
                type="tel"
                id="requestorPhone"
                name="requestorPhone"
                className="input"
                value={formData.requestorPhone}
                onChange={handleChange}
                required
                placeholder="Enter your phone number"
              />
            </div>

            <div className="form-group">
              <label htmlFor="urgency">Urgency Level *</label>
              <select
                id="urgency"
                name="urgency"
                className="input"
                value={formData.urgency}
                onChange={handleChange}
                required
              >
                <option value="LOW">Low - Can wait a few days</option>
                <option value="MEDIUM">Medium - Needed within 1-2 days</option>
                <option value="HIGH">High - Needed urgently today</option>
                <option value="CRITICAL">Critical - Life-threatening emergency</option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="message">Message to Donor</label>
              <textarea
                id="message"
                name="message"
                className="input textarea"
                value={formData.message}
                onChange={handleChange}
                rows="4"
                placeholder={`Hello ${donor.name}, I need ${donor.bloodType} blood. Please contact me if you can help.`}
              />
              <small className="form-hint">Leave blank to use default message</small>
            </div>

            <div className="modal-actions">
              <button type="button" className="btn btn-outline" onClick={onClose} disabled={loading}>
                Cancel
              </button>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Sending...' : '📤 Send Notification'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default NotificationModal;


