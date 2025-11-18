import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useLocation } from 'react-router-dom';
import ChatPage from './pages/ChatPage';
import HistoryPage from './pages/HistoryPage';

/**
 * Navigation component that highlights the active page.
 */
function Navigation() {
  const location = useLocation();

  return (
    <nav className="nav">
      <Link
        to="/"
        className={`nav-link ${location.pathname === '/' ? 'active' : ''}`}
      >
        Chat
      </Link>
      <Link
        to="/history"
        className={`nav-link ${location.pathname === '/history' ? 'active' : ''}`}
      >
        History
      </Link>
    </nav>
  );
}

/**
 * Main App component with routing.
 */
function App() {
  return (
    <Router>
      <div className="app">
        <Navigation />
        <Routes>
          <Route path="/" element={<ChatPage />} />
          <Route path="/history" element={<HistoryPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
