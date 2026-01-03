
import React, { useEffect, useState } from 'react';
import Login from './components/Login';
import UserDashboard from './components/UserDashboard';
import AdminDashboard from './components/AdminDashboard';

export default function App() {
  const [role, setRole] = useState(() => localStorage.getItem('role') || null);
  const [username, setUsername] = useState(() => localStorage.getItem('username') || '');
  const [view, setView] = useState(() => {
    const savedRole = localStorage.getItem('role');
    if (savedRole === 'admin') return 'admin';
    if (savedRole === 'user') return 'user';
    return 'login';
  });

  useEffect(() => {
    if (role) localStorage.setItem('role', role);
    else localStorage.removeItem('role');
  }, [role]);

  useEffect(() => {
    if (username) localStorage.setItem('username', username);
    else localStorage.removeItem('username');
  }, [username]);

  const onLoginSuccess = ({ role: r, username: u }) => {
    setRole(r);
    setUsername(u);
    setView(r === 'admin' ? 'admin' : 'user');
  };

  const logout = () => {
    setRole(null);
    setUsername('');
    setView('login');
  };

  return (
    <div className="app-shell">
      <header className="app-header">
        <div className="brand">Tool Review</div>
        <nav>
          {role ? (
            <>
              <span className="user-chip">{username} ({role})</span>
              <button className="btn" onClick={logout}>Logout</button>
            </>
          ) : (
            <span className="muted">Please login</span>
          )}
        </nav>
      </header>

      <main className="app-main">
        {view === 'login' && <Login onSuccess={onLoginSuccess} />}
        {view === 'user' && <UserDashboard username={username} />}
        {view === 'admin' && <AdminDashboard />}
      </main>
    </div>
  );
}
