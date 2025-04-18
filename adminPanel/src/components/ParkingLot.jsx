import React, { useEffect, useState } from 'react';
import { collection, getDocs } from 'firebase/firestore';
import { db } from '../firebase';
import './ParkingLot.css';

const ParkingLot = () => {
  const [spots, setSpots] = useState([]);

  useEffect(() => {
    const fetchSpots = async () => {
      try {
        const querySnapshot = await getDocs(collection(db, 'parkingSpots'));
        const spotsData = querySnapshot.docs.map(doc => ({
          id: doc.id,
          ...doc.data()
        }));
        setSpots(spotsData);
      } catch (error) {
        console.error('Error fetching parking spots:', error);
      }
    };

    fetchSpots();
  }, []);

  return (
    <div className="dashboard-container">
      <h1 className="dashboard-title">Parking Dashboard</h1>
      <div className="parking-grid">
        {spots.map(spot => (
          <div
            key={spot.id}
            className={`parking-card ${spot.available ? 'available' : 'occupied'}`}
          >
            <h3>{spot.location}</h3>
            <p><strong>ID:</strong> {spot.id}</p>
            <p>Status: 
              <span className="status-indicator">
                {spot.available ? '✅ Available' : '❌ Occupied'}
              </span>
            </p>
            {spot.reservedUntil && (
              <p className="reserved-time">
                Reserved Until: <br />
                {new Date(spot.reservedUntil.seconds * 1000).toLocaleString()}
              </p>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default ParkingLot;
