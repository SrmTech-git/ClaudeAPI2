import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useLocation } from 'react-router-dom';
import ChatPage from './pages/ChatPage';
import HistoryPage from './pages/HistoryPage';
import ContextPage from './pages/ContextPage';

/**
 * Navigation component that highlights the active page.
 */
function Navigation({ darkMode, toggleDarkMode }) {
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
      <Link
        to="/context"
        className={`nav-link ${location.pathname === '/context' ? 'active' : ''}`}
      >
        Context
      </Link>

      <button
        className="dark-mode-toggle"
        onClick={toggleDarkMode}
        title={darkMode ? 'Switch to light mode' : 'Switch to dark mode'}
      >
        {darkMode ? '☀️' : '🌙'}
      </button>
    </nav>
  );
}

/**
 * Main App component with routing and dark mode management.
 */
function App() {
  // Initialize dark mode from localStorage or default to false
  const [darkMode, setDarkMode] = useState(() => {
    const saved = localStorage.getItem('darkMode');
    return saved ? JSON.parse(saved) : false;
  });

  // Apply theme to document root
  useEffect(() => {
    if (darkMode) {
      document.documentElement.setAttribute('data-theme', 'dark');
    } else {
      document.documentElement.removeAttribute('data-theme');
    }
    // Save preference to localStorage
    localStorage.setItem('darkMode', JSON.stringify(darkMode));
  }, [darkMode]);

  /**
   * Toggle dark mode on/off.
   */
  const toggleDarkMode = () => {
    setDarkMode((prev) => !prev);
  };

  return (
    <Router>
      <div className="app">
        <Navigation darkMode={darkMode} toggleDarkMode={toggleDarkMode} />
        <Routes>
          <Route path="/" element={<ChatPage />} />
          <Route path="/history" element={<HistoryPage />} />
          <Route path="/context" element={<ContextPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
