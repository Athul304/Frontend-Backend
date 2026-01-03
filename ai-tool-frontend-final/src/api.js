
const BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

async function handleResponse(res) {
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(text || `Request failed with status ${res.status}`);
  }
  const contentType = res.headers.get('content-type') || '';
  if (contentType.includes('application/json')) return res.json();
  return res.text();
}

// --- Auth ---
export async function login({ username, password }) {
  const res = await fetch(`${BASE_URL}/user/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  });
  const data = await handleResponse(res); // plain text like "Admin access granted"
  const msg = typeof data === 'string' ? data : String(data);
  let role = null;
  if (/admin/i.test(msg)) role = 'admin';
  else if (/user/i.test(msg)) role = 'user';
  return { message: msg, role };
}

// --- Tools ---
export async function getTools({ category, rating } = {}) {
  const params = new URLSearchParams();
  if (category) params.append('category', category);
  if (rating !== undefined && rating !== '') params.append('rating', rating);
  const url = `${BASE_URL}/tools${params.toString() ? `?${params}` : ''}`;
  const res = await fetch(url);
  return handleResponse(res);
}

export async function addTool({ name, category, pricingType, useCase }) {
  const res = await fetch(`${BASE_URL}/tools`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, category, pricingType, useCase }),
  });
  return handleResponse(res);
}

// --- Reviews ---
export async function getReviews() {
  const res = await fetch(`${BASE_URL}/reviews`);
  return handleResponse(res);
}

export async function createReview({ username, toolId, rating, comment }) {
  const res = await fetch(`${BASE_URL}/reviews`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, toolId, rating, comment }),
  });
  return handleResponse(res);
}

export async function getPendingReviews() {
  const res = await fetch(`${BASE_URL}/admin/reviews/pending`);
  return handleResponse(res);
}

export async function updateReviewStatus({ id, status }) {
  const res = await fetch(`${BASE_URL}/admin/reviews/${id}/status`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ status }),
  });
  return handleResponse(res);
}
