import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { signInWithEmailAndPassword } from 'firebase/auth';
import { auth } from '../firebase';

// Handler interface
class LoginHandler {
  constructor(nextHandler = null) {
    this.nextHandler = nextHandler;
  }

  handle(request, response) {
    if (this.nextHandler) {
      return this.nextHandler.handle(request, response);
    }
    return response;
  }
}

// Concrete handler for input validation
class InputValidationHandler extends LoginHandler {
  handle(request, response) {
    const { email, password } = request;
    
    if (!email) {
      response.success = false;
      response.error = "Email is required";
      return response;
    }
    
    if (!password) {
      response.success = false;
      response.error = "Password is required";
      return response;
    }
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      response.success = false;
      response.error = "Invalid email format";
      return response;
    }
    
    if (password.length < 6) {
      response.success = false;
      response.error = "Password must be at least 6 characters";
      return response;
    }
    
    return super.handle(request, response);
  }
}

// Concrete handler for authentication
class AuthenticationHandler extends LoginHandler {
  async handle(request, response) {
    try {
      await signInWithEmailAndPassword(auth, request.email, request.password);
      response.success = true;
    } catch (err) {
      response.success = false;
      response.error = err.code === 'auth/user-not-found' ? 'User not found' :
                     err.code === 'auth/wrong-password' ? 'Incorrect password' :
                     err.code === 'auth/too-many-requests' ? 'Too many failed attempts, please try again later' : 
                     'Login failed: ' + err.message;
    }
    return super.handle(request, response);
  }
}

// Concrete handler for post-login actions
class PostLoginHandler extends LoginHandler {
  handle(request, response) {
    if (response.success) {
      response.redirect = () => request.navigate('/parkingspot');
    }
    return response;
  }
}

// Create the chain
const createLoginChain = () => {
  const postLoginHandler = new PostLoginHandler();
  const authHandler = new AuthenticationHandler(postLoginHandler);
  return new InputValidationHandler(authHandler);
};

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      // Create a new chain for this request
      const loginChain = createLoginChain();
      
      // Prepare the request and response objects
      const request = { email, password, navigate };
      const response = { success: false, error: null };
      
      // Process through the chain
      const result = await loginChain.handle(request, response);
      
      if (result.success) {
        result.redirect();
      } else {
        setError(result.error);
      }
    } catch (err) {
      setError('An unexpected error occurred.');
      console.error('Login error:', err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={{
      width: '100%',
      maxWidth: '400px',
      padding: '40px',
      marginLeft: '450px',
      borderRadius: '12px',
      boxShadow: '0 0 15px rgba(0, 0, 0, 0.1)',
      backgroundColor: '#fff'
    }}>
      <h2 style={{ textAlign: 'center', marginBottom: '20px', color: '#333' }}>Login</h2>
      
      <form onSubmit={handleLogin}>
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '6px', color: '#555' }}>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{
              width: '100%',
              padding: '10px',
              borderRadius: '6px',
              border: '1px solid #ccc',
              fontSize: '14px'
            }}
          />
        </div>

        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '6px', color: '#555' }}>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            style={{
              width: '100%',
              padding: '10px',
              borderRadius: '6px',
              border: '1px solid #ccc',
              fontSize: '14px'
            }}
          />
        </div>

        {error && <p style={{ color: 'red', marginBottom: '15px' }}>{error}</p>}

        <button
          type="submit"
          disabled={isLoading}
          style={{
            width: '100%',
            padding: '12px',
            marginLeft: '10px',
            borderRadius: '6px',
            backgroundColor: isLoading ? '#cccccc' : '#4CAF50',
            color: '#fff',
            fontWeight: 'bold',
            fontSize: '15px',
            border: 'none',
            cursor: 'pointer'
          }}
        >
          {isLoading ? 'Logging in...' : 'Login'}
        </button>
      </form>
    </div>
  );
};

export default Login;