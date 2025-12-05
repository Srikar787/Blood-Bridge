import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './OAuthCallback.css';

const OAuthCallback = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { login } = useAuth();

  useEffect(() => {
    const handleOAuthCallback = () => {
      // Get token and user info from URL parameters
      const token = searchParams.get('token');
      const email = searchParams.get('email');
      const name = searchParams.get('name');
      const role = searchParams.get('role');
      const error = searchParams.get('error');
      
      if (error) {
        navigate(`/login?error=${encodeURIComponent(error)}`);
        return;
      }
      
      if (token && email && name && role) {
        login({ email, name, role }, token);
        
        // Navigate based on role
        if (role === 'REQUESTOR') {
          navigate('/requestor-dashboard');
        } else {
          navigate('/donors');
        }
      } else {
        navigate('/login?error=oauth_failed');
      }
    };

    handleOAuthCallback();
  }, [navigate, login, searchParams]);

  return (
    <div className="oauth-callback">
      <div className="loading-spinner">
        <div className="spinner"></div>
        <p>Completing sign in...</p>
      </div>
    </div>
  );
};

export default OAuthCallback;

