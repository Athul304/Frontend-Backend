
import React from 'react';

export default function ReviewList({ reviews }) {
  if (!reviews || reviews.length === 0) return <div>No approved reviews yet.</div>;
  return (
    <ul className="review-list">
      {reviews.map(r => (
        <li key={r.id} className="review-item">
          <div className="review-rating">⭐ {r.rating}</div>
          <div className="review-comment">{r.comment}</div>
          <div className="review-meta">by <strong>{r.username}</strong></div>
        </li>
      ))}
    </ul>
  );
}
