import { auth } from '../firebase';
import { signInWithEmailAndPassword } from 'firebase/auth';

// authHandlers.js
export class AuthHandler {
    constructor(nextHandler = null) {
      this.nextHandler = nextHandler;
    }
  
    async handle(request) {
      if (this.nextHandler) {
        return await this.nextHandler.handle(request);
      }
      return null;
    }
  }
  
  export class CredentialValidationHandler extends AuthHandler {
    async handle({ email, password }) {
      if (!email || !password) {
        throw new Error('Email and password are required');
      }
      return super.handle({ email, password });
    }
  }
  
  export class FirebaseAuthHandler extends AuthHandler {
    constructor(nextHandler, auth) {
      super(nextHandler);
      this.auth = auth;
    }
  
    async handle({ email, password }) {
      try {
        await signInWithEmailAndPassword(this.auth, email, password);
        return { success: true, email };
      } catch (error) {
        // Convert Firebase errors to more user-friendly messages
        let message = error.message;
        if (error.code === 'auth/user-not-found') {
          message = 'No user found with this email';
        } else if (error.code === 'auth/wrong-password') {
          message = 'Incorrect password';
        }
        throw new Error(message);
      }
    }
  }
  
  export class NavigationHandler extends AuthHandler {
    constructor(nextHandler, navigate) {
      super(nextHandler);
      this.navigate = navigate;
    }
  
    async handle(response) {
      if (response.success) {
        this.navigate('/parkingspot');
      }
      return super.handle(response);
    }
  }
  
  export class ErrorHandler extends AuthHandler {
    async handle(error) {
      // This would be the end of the chain
      throw error; // Or you could return a standardized error object
    }
  }