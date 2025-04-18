import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { signInWithEmailAndPassword } from 'firebase/auth';
import { auth } from '../firebase';

// Chain of operations handlers
const loginOperations = {
  // Validate inputs before submission
  validateInputs: (email, password) => {
    if (!email) return { success: false, error: "Email is required" };
    if (!password) return { success: false, error: "Password is required" };
    
    // Basic email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) return { success: false, error: "Invalid email format" };
    
    // Basic password validation
    if (password.length < 6) return { success: false, error: "Password must be at least 6 characters" };
    
    return { success: true };
  },
  
  // Handle the authentication with Firebase
  authenticate: async (email, password) => {
    try {
      await signInWithEmailAndPassword(auth, email, password);
      return { success: true };
    } catch (err) {
      return { 
        success: false, 
        error: err.code === 'auth/user-not-found' ? 'User not found' :
               err.code === 'auth/wrong-password' ? 'Incorrect password' :
               err.code === 'auth/too-many-requests' ? 'Too many failed attempts, please try again later' : 
               'Login failed: ' + err.message
      };
    }
  },
  
  // Handle successful login actions
  handleSuccess: (navigate) => {
    // You could add more actions here like setting user data in context/redux
    // Or maybe fetching initial user data before redirecting
    return { 
      success: true,
      redirect: () => navigate('/parkingspot')
    };
  }
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
      // Step 1: Validate inputs
      const validationResult = loginOperations.validateInputs(email, password);
      if (!validationResult.success) {
        setError(validationResult.error);
        setIsLoading(false);
        return;
      }

      // Step 2: Authenticate with Firebase
      const authResult = await loginOperations.authenticate(email, password);
      if (!authResult.success) {
        setError(authResult.error);
        setIsLoading(false);
        return;
      }

      // Step 3: Handle success and redirect
      const successResult = loginOperations.handleSuccess(navigate);
      if (successResult.success) {
        successResult.redirect();
      }
    } catch (err) {
      setError('An unexpected error occurred.');
      console.error('Login error:', err);
    } finally {
      setIsLoading(false);
    }
  };

return ( <div style={{          width: '100%',         maxWidth: '400px',         padding: '40px',         marginLeft: '450px',         borderRadius: '12px',         boxShadow: '0 0 15px rgba(0, 0, 0, 0.1)',         backgroundColor: '#fff'       }}>         <h2 style={{ textAlign: 'center', marginBottom: '20px', color: '#333' }}>Login</h2>              
<form onSubmit={handleLogin}>           <div style={{ marginBottom: '20px' }}>             <label style={{ display: 'block', marginBottom: '6px', color: '#555' }}>Email</label>             <input               type="email"               value={email}               onChange={(e) => setEmail(e.target.value)}               required               style={{                 width: '100%',                 padding: '10px',                 borderRadius: '6px',                 border: '1px solid #ccc',                 fontSize: '14px'               }}             />           </div>                <div style={{ marginBottom: '20px' }}>             <label style={{ display: 'block', marginBottom: '6px', color: '#555' }}>Password</label>             <input               type="password"               value={password}               onChange={(e) => setPassword(e.target.value)}               required               style={{                 width: '100%',                 padding: '10px',                 borderRadius: '6px',                 border: '1px solid #ccc',                 fontSize: '14px'               }}             />           </div>                {error && <p style={{ color: 'red', marginBottom: '15px' }}>{error}</p>}                <button             type="submit"             style={{               width: '100%',               padding: '12px',               marginLeft: '10px',               borderRadius: '6px',               backgroundColor: '#4CAF50',               color: '#fff',               fontWeight: 'bold',               fontSize: '15px',               border: 'none',               cursor: 'pointer'             }}           >             Login           </button>         </form>
</div>        )
};

export default Login;