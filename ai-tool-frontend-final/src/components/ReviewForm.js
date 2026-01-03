
import React, { useState } from 'react';

export default function ReviewForm({ onSubmit }) {
  const [rating, setRating] = useState('');
  const [comment, setComment] = useState('');

  const submit = (e) => {
    e.preventDefault();
    if (rating === '' || Number.isNaN(Number(rating))) return alert('Rating is required');
    onSubmit({ rating, comment });
    setRating('');
    setComment('');
  };

  return (
    <form onSubmit={submit} className="form-grid">
      <label>
        Rating (0–5)
        <input
          type="number"
          min="0" max="5" step="0.5"
          value={rating}
          onChange={(e) => setRating(e.target.value)}
          required
        />
      </label>
      <label>
        Comment
        <textarea
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          rows={3}
          placeholder="Your thoughts..."
        />
      </label>
      <button className="btn primary" type="submit">Submit Review</button>
    </form>
  );
}
