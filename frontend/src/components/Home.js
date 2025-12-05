import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Home.css';

const Home = () => {
  const { isAuthenticated, user } = useAuth();

  return (
    <div className="home">
      <div className="hero-section">
        <div className="hero-content">
          <h1 className="hero-title">
            Save Lives, One Drop at a Time
          </h1>
          <p className="hero-subtitle">
            Connect blood donors with those in urgent need. Join our community and make a difference today.
          </p>
          {!isAuthenticated ? (
            <div className="hero-actions">
              <Link to="/signup" className="btn btn-primary btn-large">
                Get Started
              </Link>
              <Link to="/login" className="btn btn-outline btn-large">
                Sign In
              </Link>
            </div>
          ) : (
            <div className="hero-actions">
              {user?.role === 'REQUESTOR' ? (
                <Link to="/requestor-dashboard" className="btn btn-primary btn-large">
                  Find Donors
                </Link>
              ) : (
                <Link to="/add-donor" className="btn btn-primary btn-large">
                  Register as Donor
                </Link>
              )}
            </div>
          )}
        </div>
        <div className="hero-image">
          <div className="floating-card card-1">💉</div>
          <div className="floating-card card-2">🩸</div>
          <div className="floating-card card-3">❤️</div>
        </div>
      </div>

      <div className="features-section">
        <h2 className="section-title">Why Choose BloodBridge?</h2>
        <div className="features-grid">
          <div className="feature-card">
            <div className="feature-icon">🔍</div>
            <h3>Find Donors Fast</h3>
            <p>Search for eligible donors within 5-10km radius using location-based matching</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">✅</div>
            <h3>Verified Eligibility</h3>
            <p>All donors are verified for age, health, and donation interval requirements</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">📍</div>
            <h3>Location-Based</h3>
            <p>Find nearby donors quickly with GPS or manual location input</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🔐</div>
            <h3>Secure & Private</h3>
            <p>Your data is protected with secure authentication and privacy controls</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">⚡</div>
            <h3>Quick Response</h3>
            <p>Connect with donors instantly through phone or email</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🤝</div>
            <h3>Community Driven</h3>
            <p>Join thousands of donors making a difference in their communities</p>
          </div>
        </div>
      </div>

      <div className="stats-section">
        <div className="stat-card">
          <div className="stat-number">10K+</div>
          <div className="stat-label">Active Donors</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">5K+</div>
          <div className="stat-label">Lives Saved</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">50+</div>
          <div className="stat-label">Cities</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">24/7</div>
          <div className="stat-label">Available</div>
        </div>
      </div>

      <div className="cta-section">
        <h2>Ready to Make a Difference?</h2>
        <p>Join BloodBridge today and help save lives in your community</p>
        {!isAuthenticated && (
          <Link to="/signup" className="btn btn-primary btn-large">
            Get Started Now
          </Link>
        )}
      </div>
    </div>
  );
};

export default Home;
