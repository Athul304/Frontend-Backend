
import React, { useEffect, useState } from 'react';
import { getPendingReviews, updateReviewStatus, getTools, addTool } from '../api';

export default function AdminDashboard() {
  const [tab, setTab] = useState('pending'); // 'pending' | 'tools'

  return (
    <div>
      <h2>Admin Dashboard</h2>
      <nav className="tabs">
        <button className={`btn ${tab === 'pending' ? 'primary' : ''}`} onClick={() => setTab('pending')}>
          Pending Reviews
        </button>
        <button className={`btn ${tab === 'tools' ? 'primary' : ''}`} onClick={() => setTab('tools')}>
          Manage Tools
        </button>
      </nav>
      <div style={{ marginTop: 12 }}>
        {tab === 'pending' ? <PendingReviews /> : <ToolsAdmin />}
      </div>
    </div>
  );
}

function PendingReviews() {
  const [pending, setPending] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState('');

  const load = async () => {
    setLoading(true);
    setErr('');
    try {
      const data = await getPendingReviews();
      setPending(Array.isArray(data) ? data : []);
    } catch {
      setErr('Failed to load pending reviews');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const changeStatus = async (id, status) => {
    try {
      await updateReviewStatus({ id, status });
      await load();
    } catch {
      alert('Failed to update status');
    }
  };

  return (
    <div className="card">
      <h3>Pending Reviews</h3>
      {loading ? <div>Loading...</div> :
       err ? <div className="error">{err}</div> :
       pending.length === 0 ? <div>No pending reviews.</div> :
       <table className="table">
         <thead>
           <tr>
             <th>ID</th><th>Tool ID</th><th>User</th><th>Rating</th><th>Comment</th><th>Status</th><th>Actions</th>
           </tr>
         </thead>
         <tbody>
           {pending.map(r => (
             <tr key={r.id}>
               <td>{r.id}</td>
               <td>{r.toolId}</td>
               <td>{r.username}</td>
               <td>{r.rating}</td>
               <td>{r.comment}</td>
               <td>{r.status}</td>
               <td>
                 <button className="btn primary" onClick={() => changeStatus(r.id, 'APPROVED')}>Approve</button>
                 <button className="btn danger" onClick={() => changeStatus(r.id, 'REJECTED')}>Reject</button>
               </td>
             </tr>
           ))}
         </tbody>
       </table>}
    </div>
  );
}

function ToolsAdmin() {
  const [tools, setTools] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState('');
  const [form, setForm] = useState({ name: '', category: '', pricingType: 'FREE', useCase: '' });

  const load = async () => {
    setLoading(true);
    setErr('');
    try {
      const data = await getTools();
      setTools(Array.isArray(data) ? data : []);
    } catch {
      setErr('Failed to load tools');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const submit = async (e) => {
    e.preventDefault();
    try {
      const created = await addTool(form);
      alert(`Tool created: ${created.name} (ID ${created.id})`);
      setForm({ name: '', category: '', pricingType: 'FREE', useCase: '' });
      await load();
    } catch {
      alert('Failed to add tool');
    }
  };

  return (
    <div className="grid">
      <section className="card">
        <h3>Add Tool</h3>
        <form onSubmit={submit} className="form-grid">
          <label>
            Name
            <input value={form.name} onChange={(e) => setForm(f => ({ ...f, name: e.target.value }))} required />
          </label>
          <label>
            Category
            <input value={form.category} onChange={(e) => setForm(f => ({ ...f, category: e.target.value }))} required />
          </label>
          <label>
            Pricing Type
            <select value={form.pricingType} onChange={(e) => setForm(f => ({ ...f, pricingType: e.target.value }))}>
              <option value="FREE">FREE</option>
              <option value="SUBSCRIPTION">SUBSCRIPTION</option>
              <option value="ONE_TIME">ONE_TIME</option>
            </select>
          </label>
          <label>
            Use Case
            <input value={form.useCase} onChange={(e) => setForm(f => ({ ...f, useCase: e.target.value }))} required />
          </label>
          <button className="btn primary" type="submit">Create</button>
        </form>
      </section>

      <section className="card">
        <h3>Existing Tools</h3>
        {loading ? <div>Loading...</div> :
         err ? <div className="error">{err}</div> :
         tools.length === 0 ? <div>No tools.</div> :
         <table className="table">
           <thead>
             <tr>
               <th>ID</th><th>Name</th><th>Category</th><th>Pricing</th><th>Use Case</th><th>Avg Rating</th>
             </tr>
           </thead>
           <tbody>
             {tools.map(t => (
               <tr key={t.id}>
                 <td>{t.id}</td>
                 <td>{t.name}</td>
                 <td>{t.category}</td>
                 <td>{t.pricingType}</td>
                 <td>{t.useCase}</td>
                 <td>{Number(t.averageRating || 0).toFixed(1)}</td>
               </tr>
             ))}
           </tbody>
         </table>}
      </section>
    </div>
  );
}
