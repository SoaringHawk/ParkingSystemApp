import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import ParkingLot from './components/ParkingLot'; 

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/parkingspot" element={<ParkingLot />} />
      </Routes>
    </Router>
  );
}
export default App;