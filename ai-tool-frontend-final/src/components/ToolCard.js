
import React from 'react';

export default function ToolCard({ tool, onView, selected }) {
  return (
    <div className={`tool-card ${selected ? 'selected' : ''}`}>
      <div className="tool-header">
        <strong>{tool.name}</strong>
        <span className="badge">{tool.category}</span>
      </div>
      <div className="tool-body">
        <div>Pricing: <em>{tool.pricingType}</em></div>
        <div>Use case: {tool.useCase}</div>
        <div>Average rating: <strong>{Number(tool.averageRating || 0).toFixed(1)}</strong></div>
      </div>
      <div className="tool-actions">
        <button className="btn" onClick={onView}>View Reviews</button>
      </div>
    </div>
  );
}
