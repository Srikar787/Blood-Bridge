import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          <span className="navbar-logo">🩸</span>
          <span className="navbar-title">BloodBridge</span>
        </Link>
        
        <div className="navbar-menu">
          {isAuthenticated ? (
            <>
              {/* All authenticated users can view donors list */}
              <Link to="/donors" className="navbar-link">Donors</Link>

              {/* Common actions for any authenticated user */}
              <Link to="/add-donor" className="navbar-link">Register as Donor</Link>
              <Link to="/requestor-dashboard" className="navbar-link">Find Donors</Link>

              <div className="navbar-user">
                <span className="user-name">{user?.name || user?.email}</span>
                <button onClick={handleLogout} className="btn-logout">
                  Logout
                </button>
              </div>
            </>
          ) : (
            <>
              <Link to="/" className="navbar-link">Home</Link>
              <Link to="/login" className="navbar-link">Login</Link>
              <Link to="/signup" className="btn btn-primary btn-nav">
                Sign Up
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
