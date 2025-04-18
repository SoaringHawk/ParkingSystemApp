// Import the functions you need from the SDKs you need
import { initializeApp } from 'firebase/app';
//import { getAnalytics } from 'firebase/analytics';
import { getAuth } from 'firebase/auth';
import { getFirestore } from 'firebase/firestore'; // 🔥 Add this line to use Firestore

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyCQ-q74QCFFnQkMkU-pBlOY7zNScJ1nG9c",
  authDomain: "parkingreservationapp-824d5.firebaseapp.com",
  databaseURL: "https://parkingreservationapp-824d5-default-rtdb.firebaseio.com",
  projectId: "parkingreservationapp-824d5",
  storageBucket: "parkingreservationapp-824d5.firebasestorage.app",
  messagingSenderId: "901228917393",
  appId: "1:901228917393:web:74912ea434bc0c9a948530",
  measurementId: "G-REHW17S6MS"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
//const analytics = getAnalytics(app);

export const auth = getAuth(app);
export const db = getFirestore(app); // 🔥 Export Firestore
