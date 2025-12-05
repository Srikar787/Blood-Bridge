import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import './App.css';
import Navbar from './components/Navbar';
import Home from './components/Home';
import Login from './components/Login';
import Signup from './components/Signup';
import DonorsList from './components/DonorsList';
import AddDonor from './components/AddDonor';
import RequestorDashboard from './components/RequestorDashboard';
import OAuthCallback from './components/OAuthCallback';

const PrivateRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  
  if (loading) {
    return <div className="loading-screen">Loading...</div>;
  }
  
  return isAuthenticated ? children : <Navigate to="/login" />;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Navbar />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/auth/callback" element={<OAuthCallback />} />
            <Route 
              path="/donors" 
              element={
                <PrivateRoute>
                  <div className="container"><DonorsList /></div>
                </PrivateRoute>
              } 
            />
            <Route 
              path="/add-donor" 
              element={
                <PrivateRoute>
                  <div className="container"><AddDonor /></div>
                </PrivateRoute>
              } 
            />
            <Route 
              path="/requestor-dashboard" 
              element={
                <PrivateRoute>
                  <RequestorDashboard />
                </PrivateRoute>
              } 
            />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
