# Tool Review App

A full-stack web application for discovering AI tools and managing user reviews, built with **Spring Boot** (backend) and **React** (frontend).  
The system supports **role-based access** with separate dashboards for **Admins** and **Users**, including a review moderation workflow.

---

## Features

### Authentication & Roles
- Simple username/password login (demo-level, no JWT or session management)
- Role-based navigation:
  - **ADMIN**
  - **USER**

> This authentication approach is intentionally lightweight and **not production-ready**.

---

### Admin Features
- Add new AI tools
- View all existing tools with:
  - Category
  - Pricing type
  - Use case
  - Average rating
- Review moderation:
  - View **PENDING** reviews
  - **Approve** or **Reject** reviews
- Approved reviews automatically update tool average ratings

---

### User Features
- Browse AI tools
- Filter tools by:
  - Category
  - Minimum average rating
- View approved reviews for each tool
- Submit reviews (rating + comment)
  - Reviews are visible only after admin approval

---

## Screenshots
- Login screen 
- <img width="389" height="387" alt="image" src="https://github.com/user-attachments/assets/28296716-602a-4fe6-8c74-ea8658aa393b" />
- Admin tool management
- <img width="960" height="560" alt="image (4)" src="https://github.com/user-attachments/assets/57f6513b-4eb5-4cec-a9a8-a2fbc59d5102" />
- Admin review moderation   
- <img width="959" height="562" alt="image (3)" src="https://github.com/user-attachments/assets/61fe02f0-1db2-4fb9-9cc5-f3c257564d7b" />
- User dashboard with filters
- <img width="959" height="564" alt="image (1)" src="https://github.com/user-attachments/assets/5978ae69-37a2-4127-b514-fef052216798" />
- Reviews and review submission
- <img width="960" height="563" alt="image (2)" src="https://github.com/user-attachments/assets/031727e4-8b3c-4217-b758-b08067af93c3" />

---

## Tech Stack

### Backend
- Java 17+
- Spring Boot
- Spring Web (REST APIs)
- Spring Data JPA
- Hibernate
- MySQL
- Maven

### Frontend
- React (Create React App)
- JavaScript (ES6+)
- Fetch API
- Plain CSS (dark theme)

---

## Backend Architecture
```text
src/main/java/com/example/ai_tool_finder
├── controller
├── service
├── repository
├── model
├── dto
├── exception
└── specification
```

### Backend Highlights
- DTO-based API design
- Global exception handling
- JPA Specifications for dynamic filtering
- Enum-driven state management (`PENDING`, `APPROVED`, `REJECTED`)
- Proper entity relationships:
  - User ↔ Review ↔ Tool

---

## Frontend Architecture
```text
src/
├── components/
│   ├── Login.jsx
│   ├── AdminDashboard.jsx
│   ├── UserDashboard.jsx
│   └── ToolReviews.jsx
├── api.js
├── App.js
└── index.js
```


### Frontend Notes
- Role stored in `localStorage` after login
- Backend base URL configured via environment variables
- No routing library; navigation is state-driven

---

## Environment Setup

### Backend (`application.properties`)

```
spring.datasource.url=jdbc:mysql://localhost:3306/tool_review_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### Frontend (`.env`)
REACT_APP_API_BASE_URL=http://localhost:8080

---

## Running the Application

### Backend
Backend runs on: `http://localhost:8080`

---

### Frontend
Frontend runs on: `http://localhost:3000`

---

## API Overview

### Authentication
- `POST /login`

### Tools
- `GET /tools`
- `POST /admin/tools`
- `GET /tools/filter`

### Reviews
- `POST /reviews`
- `GET /tools/{id}/reviews`
- `GET /admin/reviews/pending`
- `POST /admin/reviews/{id}/approve`
- `POST /admin/reviews/{id}/reject`

---

## Known Limitations

- No Spring Security or JWT
- Passwords are not encrypted
- No pagination or sorting
- No deployment configuration
- Minimal frontend styling

This project is intended for **learning and demonstration purposes**, not production use.
