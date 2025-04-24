import React, { useEffect, useState } from 'react';
import { collection, getDocs, doc, updateDoc, deleteDoc } from 'firebase/firestore';
import { db } from '../firebase';
import './ParkingLot.css';

const ParkingLot = () => {
  const [spots, setSpots] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [activeTab, setActiveTab] = useState('parking');
  const [editingItem, setEditingItem] = useState(null);
  const [editFormData, setEditFormData] = useState({});
  const [showEditModal, setShowEditModal] = useState(false);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      // Fetch parking spots
      const spotsSnapshot = await getDocs(collection(db, 'parkingSpots'));
      const spotsData = spotsSnapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data(),
        reservedUntil: doc.data().reservedUntil?.toDate()
      }));
      setSpots(spotsData);

      // Fetch reservations
      const resSnapshot = await getDocs(collection(db, 'reservations'));
      const resData = resSnapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data(),
        startTime: doc.data().startTime?.toDate(),
        endTime: doc.data().endTime?.toDate(),
        paymentTimestamp: doc.data().paymentTimestamp?.toDate()
      }));
      setReservations(resData);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const formatDate = (date) => {
    if (!date) return 'N/A';
    return date.toLocaleString();
  };

  const handleEditClick = (item, type) => {
    setEditingItem({ ...item, type });
    setEditFormData({
      ...item,
      startTime: item.startTime?.toISOString().slice(0, 16),
      endTime: item.endTime?.toISOString().slice(0, 16),
      reservedUntil: item.reservedUntil?.toISOString().slice(0, 16)
    });
    setShowEditModal(true);
  };

  const handleCancelReservation = async (reservationId) => {
    if (window.confirm('Are you sure you want to cancel this reservation?')) {
      try {
        await deleteDoc(doc(db, 'reservations', reservationId));
        
        // Also update the associated parking spot to available
        const reservation = reservations.find(r => r.id === reservationId);
        if (reservation) {
          const spotRef = doc(db, 'parkingSpots', reservation.spotId);
          await updateDoc(spotRef, {
            available: true,
            reservedUntil: null
          });
        }
        
        fetchData(); // Refresh data
        alert('Reservation cancelled successfully!');
      } catch (error) {
        console.error('Error cancelling reservation:', error);
        alert('Failed to cancel reservation');
      }
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setEditFormData({
      ...editFormData,
      [name]: type === 'checkbox' ? checked : value
    });
  };

  const handleSave = async () => {
    try {
      if (editingItem.type === 'parking') {
        const spotRef = doc(db, 'parkingSpots', editingItem.id);
        await updateDoc(spotRef, {
          location: editFormData.location,
          available: editFormData.available === 'true' || editFormData.available === true,
          reservedUntil: editFormData.reservedUntil ? new Date(editFormData.reservedUntil) : null
        });
      }
      setShowEditModal(false);
      fetchData(); // Refresh data
      alert('Update successful!');
    } catch (error) {
      console.error('Error updating document:', error);
      alert('Update failed!');
    }
  };

  return (
    <div className="dashboard-container">
      <h1 className="dashboard-title">Parking Management Dashboard</h1>
      
      <div className="tabs">
        <button 
          className={`tab-button ${activeTab === 'parking' ? 'active' : ''}`}
          onClick={() => setActiveTab('parking')}
        >
          Parking Spots
        </button>
        <button 
          className={`tab-button ${activeTab === 'reservations' ? 'active' : ''}`}
          onClick={() => setActiveTab('reservations')}
        >
          Reservations
        </button>
      </div>

      {activeTab === 'parking' && (
        <div className="table-container">
          <h2>Parking Spots</h2>
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Location</th>
                <th>Status</th>
                <th>Reserved Until</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {spots.map(spot => (
                <tr key={spot.id} className={spot.available ? 'available' : 'occupied'}>
                  <td>{spot.id}</td>
                  <td>{spot.location || 'N/A'}</td>
                  <td>
                    <span className={`status-indicator ${spot.available ? 'available' : 'occupied'}`}>
                      {spot.available ? '✅ Available' : '❌ Occupied'}
                    </span>
                  </td>
                  <td>
                    {spot.reservedUntil ? formatDate(spot.reservedUntil) : 'Not reserved'}
                  </td>
                  <td>
                    <button 
                      className="edit-button"
                      onClick={() => handleEditClick(spot, 'parking')}
                    >
                      Edit
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {activeTab === 'reservations' && (
        <div className="table-container">
          <h2>Reservations</h2>
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Spot ID</th>
                <th>User</th>
                <th>License Plate</th>
                <th>Start Time</th>
                <th>End Time</th>
                <th>Price</th>
                <th>Paid</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {reservations.map(res => (
                <tr key={res.id}>
                  <td>{res.id}</td>
                  <td>{res.spotId}</td>
                  <td>{res.userId || 'N/A'}</td>
                  <td>{res.licensePlate}</td>
                  <td>{formatDate(res.startTime)}</td>
                  <td>{formatDate(res.endTime)}</td>
                  <td>${res.price?.toFixed(2) || '0.00'}</td>
                  <td className={res.paid ? 'paid' : 'unpaid'}>
                    {res.paid ? '✅' : '❌'}
                  </td>
                  <td>
                    <button 
                      className="cancel-button"
                      onClick={() => handleCancelReservation(res.id)}
                    >
                      Cancel
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showEditModal && editingItem && editingItem.type === 'parking' && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>Edit Parking Spot</h3>
            
            <div className="form-group">
              <label>Location:</label>
              <input
                type="text"
                name="location"
                value={editFormData.location || ''}
                onChange={handleInputChange}
              />
            </div>
            <div className="form-group">
              <label>Status:</label>
              <select
                name="available"
                value={editFormData.available}
                onChange={handleInputChange}
              >
                <option value={true}>Available</option>
                <option value={false}>Occupied</option>
              </select>
            </div>
            <div className="form-group">
              <label>Reserved Until:</label>
              <input
                type="datetime-local"
                name="reservedUntil"
                value={editFormData.reservedUntil || ''}
                onChange={handleInputChange}
              />
            </div>

            <div className="modal-actions">
              <button className="secondary-button" onClick={() => setShowEditModal(false)}>
                Close
              </button>
              <button className="primary-button" onClick={handleSave}>
                Save Changes
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ParkingLot;