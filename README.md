# 🎮 GameHub Capstone Project <BR> @NeueFische Java Fullstack Bootcamp

## 📖 Description
**GameHub** is a web application designed to help users manage their game collections. With GameHub, users can:
- Add games to their library 🕹️
- Write reviews and ratings ✍️⭐
- Track their progress and goals 🎯
- Explore new games using real-time data fetched from external APIs like **IGDB** 🌐

It’s the ultimate tool for any gamer who wants to keep track of their gaming adventures in one place!

## 🚀 Features (Planned)
- **User Authentication** 🔒: Secure sign-up and login functionality.
- **Personal Game Library** 📚: Manage your game collection, categorize them, and add personal notes.
- **Game Reviews & Ratings** 📝: Share your thoughts and give star ratings for each game.
- **Game Search** 🔍: Search for new games by name using IGDB’s extensive database.
- **Progress Tracking** ⏳: Set goals, track achievements, and monitor completion percentages for each game.
- **Responsive Design** 📱: A modern and mobile-friendly UI that looks great on any device.
- **Interactive Dashboard** 📊 (Future): Visualize gaming statistics and completion goals.

## 🛠️ Tech Stack
### Backend
- **Java 22** ☕: Modern Java for fast, reliable performance.
- **Spring Boot** 🌱: Backend framework for building RESTful APIs and handling business logic.
- **MongoDB** 🍃: NoSQL database for flexible and scalable data storage.
- **RestTemplate/WebClient** 📡: For interacting with external APIs like IGDB.

### Frontend
- **React** ⚛️: Frontend library for building interactive user interfaces.
- **TypeScript** 🛡️: Statically typed JavaScript for better code reliability and development.
- **Vite** ⚡: A fast, modern build tool for frontend projects.
- **React Router** 🧭: Handle navigation and routing in the app.
- **Chakra UI** 🎨: A sleek and customizable component library for building a responsive UI.

### CI/CD
- **GitHub Actions**: Automates builds, tests, and deployment. Triggers workflows for both frontend and backend when code is pushed or a pull request is made.
- **Docker**: Image creation and containerization on Render
- **Render**: Hosts the App, providing automatic deployment on each push to the main branch with zero-downtime updates.

### External API Integration
- **IGDB API** 🎮: Fetch detailed game information (names, covers, genres) using **IGDB's** vast database.
    - **Twitch Authentication** 🧩: Used for authentication to access the IGDB API.

## 📦 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/SimonSta95/GameHub_Capstone.git

### Backend
2. Install dependencies and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   
3. Ensure MongoDB is running locally or connected to a MongoDB Atlas cluster.

4. The backend will be available at http://localhost:8080/.
### Frontend

1. Install the required packages and run the development server:
   ```bash
   npm install
   npm run dev

2. The frontend will be available at http://localhost:5173/.
