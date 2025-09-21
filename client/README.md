# 🏠 Student Room Rental Platform

A fullstack web application for managing and renting student housing.  
Developed with **Vue.js** (frontend) and **Spring Boot (Java)** (backend).  
Includes **map visualization with Leaflet** and **spatial data powered by PostGIS**.

---

## 🚀 Features

- **Search & Listings**
  - Search rooms by city, university, and region.
  - Apply filters specially designed for students: distance from campus, min and max price, etc.
  - Sort listings by price, distance, or most recent.

- **Interactive Map**
  - Display available rooms on a map with Leaflet.
  - See distance to university and room price directly on map markers.
  - Filter results directly from the map view.

- **Room Details**
  - Full detail page with photos, description, equipment, and house rules.
  - Reviews and ratings from previous students.
  - Contact information of the landlord.
  - Related rooms in the same apartment.

- **User Features**
  - Register, login, and manage personal profile.
  - Publish new listings with photos and detailed room info.
  - Save favorite listings for quick access.
  - Manage own listings (edit or delete).
  - View favorite rooms and personal ads from the dashboard.

- **Admin Features**
  - Manage users (view, deactivate, or delete).
  - Manage published listings.
  - Manage universities/centers (create, edit, delete).
  - Visualize all users’ ads and details.

---

## 🛠️ Tech Stack

**Frontend**
- Vue.js 3  
- Vue Router  
- Axios  
- Leaflet (maps)

**Backend**
- Java 17  
- Spring Boot  
- Spring Data JPA / Hibernate  
- Postgres + PostGIS (spatial data support)

---

## 📂 Repository Structure

student-room-rental/
│── backend/ # Spring Boot REST API
│── frontend/ # Vue.js application
│── docs/ # Documentation, diagrams, mockups, screenshots
└── README.md


---

## ⚙️ How to Run

### 1. Backend
cd backend
./mvnw spring-boot:run

### 2. Frontend
cd frontend
npm install
npm run serve

---
## 📸 Screenshots

--
## 🎯 Motivation

This project was originally developed as my Bachelor’s Thesis.
It simulates a real marketplace for student housing, applying modern web development and geospatial technologies.

It demonstrates skills in:

Fullstack development (Vue.js + Spring Boot).

Handling geospatial data with PostGIS + Leaflet.

Building scalable, modular applications with user and admin roles.

--
## 👨‍💻 Author
**Jorge Arias Barrio**  
[GitHub](https://github.com/jorgeariasbarrio) | | [LinkedIn](https://www.linkedin.com/in/jorge-arias-barrio-17429198/)


