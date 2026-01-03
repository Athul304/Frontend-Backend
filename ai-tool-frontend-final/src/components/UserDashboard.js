
import React, { useEffect, useMemo, useState } from 'react';
import { getTools, getReviews, createReview } from '../api';
import ToolCard from './ToolCard';
import ReviewList from './ReviewList';
import ReviewForm from './ReviewForm';

export default function UserDashboard({ username }) {
  const [tools, setTools] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [selectedTool, setSelectedTool] = useState(null);

  const [filters, setFilters] = useState({ category: '', rating: '' });
  const [loadingTools, setLoadingTools] = useState(false);
  const [loadingReviews, setLoadingReviews] = useState(false);
  const [errTools, setErrTools] = useState('');
  const [errReviews, setErrReviews] = useState('');

  const loadTools = async () => {
    setLoadingTools(true);
    setErrTools('');
    try {
      const data = await getTools({
        category: filters.category || undefined,
        rating: filters.rating ? Number(filters.rating) : undefined,
      });
      setTools(Array.isArray(data) ? data : []);
    } catch (e) {
      setErrTools('Failed to load tools');
    } finally {
      setLoadingTools(false);
    }
  };

  const loadReviews = async () => {
    setLoadingReviews(true);
    setErrReviews('');
    try {
      const data = await getReviews();
      setReviews(Array.isArray(data) ? data : []);
    } catch (e) {
      setErrReviews('Failed to load reviews');
    } finally {
      setLoadingReviews(false);
    }
  };

  useEffect(() => { loadTools(); }, [filters.category, filters.rating]);
  useEffect(() => { loadReviews(); }, []);

  const categories = useMemo(() => {
    const s = new Set(tools.map(t => t.category).filter(Boolean));
    return Array.from(s).sort();
  }, [tools]);

  const approvedReviewsForSelected = useMemo(() => {
    if (!selectedTool) return [];
    return reviews.filter(r => r.toolId === selectedTool.id && r.status === 'APPROVED');
  }, [reviews, selectedTool]);

  const submitReview = async ({ rating, comment }) => {
    if (!selectedTool) return;
    try {
      const payload = { username, toolId: selectedTool.id, rating: Number(rating), comment };
      const res = await createReview(payload);
      alert(`Review submitted. Status: ${res.status}`);
      await loadReviews();
    } catch {
      alert('Failed to submit review');
    }
  };

  return (
    <div>
      <h2>User Dashboard</h2>

      <section className="card">
        <h3>Filter Tools</h3>
        <div className="form-grid">
          <label>
            Category
            <select
              value={filters.category}
              onChange={(e) => setFilters(f => ({ ...f, category: e.target.value }))}
            >
              <option value="">(All)</option>
              {categories.map(c => <option key={c} value={c}>{c}</option>)}
            </select>
          </label>
          <label>
            Min Rating
            <input
              type="number" step="0.1" min="0" max="5"
              value={filters.rating}
              onChange={(e) => setFilters(f => ({ ...f, rating: e.target.value }))}
              placeholder="e.g. 4.2"
            />
          </label>
          <div>
            <button className="btn" onClick={loadTools}>Apply</button>
            <button className="btn" onClick={() => setFilters({ category: '', rating: '' })}>Reset</button>
          </div>
        </div>
      </section>

      <section className="card">
        <h3>Tools</h3>
        {loadingTools ? <div>Loading tools...</div> :
         errTools ? <div className="error">{errTools}</div> :
         tools.length === 0 ? <div>No tools found.</div> :
         <div className="tool-grid">
           {tools.map(tool => (
             <ToolCard
               key={tool.id}
               tool={tool}
               onView={() => setSelectedTool(tool)}
               selected={selectedTool?.id === tool.id}
             />
           ))}
         </div>}
      </section>

      {selectedTool && (
        <section className="card">
          <h3>{selectedTool.name} — Approved Reviews</h3>
          {loadingReviews ? <div>Loading reviews...</div> :
           errReviews ? <div className="error">{errReviews}</div> :
           <ReviewList reviews={approvedReviewsForSelected} />}
          <div className="divider" />
          <h4>Write a Review</h4>
          <ReviewForm onSubmit={submitReview} />
        </section>
      )}
    </div>
  );
}
