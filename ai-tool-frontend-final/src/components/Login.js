

import React, { useState } from 'react';
import { login } from '../api';

export default function Login({ onSuccess }) {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const { role, message } = await login(form);
      if (!role) {
        setError(message || 'Login failed');
      } else {
        onSuccess({ role, username: form.username });
      }
    } catch (err) {
      setError(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h1>Login</h1>
      <form onSubmit={submit} className="form-grid">
        <label>
          Username
          <input
            value={form.username}
            onChange={(e) => setForm(f => ({ ...f, username: e.target.value }))}
            required
          />
        </label>
        <label>
          Password
          <input
            type="password"
            value={form.password}
            onChange={(e) => setForm(f => ({ ...f, password: e.target.value }))}
            required
          />
        </label>
        <button className="btn primary" type="submit" disabled={loading}>
          {loading ? 'Signing in...' : 'Login'}
        </button>
      </form>
      {error && <div className="error">{error}</div>}
      <p className="hint">Role-based navigation happens automatically after login.</p>
    </div>
  );
}
