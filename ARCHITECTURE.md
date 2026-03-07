**BloodBridge — Architecture Overview**

This document summarizes the project's architecture, the main technologies used, reasons for choosing them, and tradeoffs to be aware of. It is written for developers and stakeholders who want a concise view of why the stack was chosen and where to look in the code.

1) High-level overview
- Frontend: React Single-Page Application (Create React App). Renders client-side, handles routing, forms, and user interactions. Builds to `frontend/build` for production static hosting.
- Backend: Spring Boot (Java) REST API. Provides secure endpoints, business logic, integration with external services (email/SMS), and data persistence.
- Database: MongoDB (document store). Stores `users`, `donors`, `notifications` and supports geospatial queries for proximity search.

2) Core responsibilities
- Frontend (`frontend/`): UI components (`src/components/`), routing (`src/App.js`), auth state (`src/context/AuthContext.js`), API endpoints (`src/config.js`). Uses `axios` for HTTP and stores JWT in `localStorage`.
- Backend (`backend/`): Controllers (`controller/`) expose REST endpoints, Services (`service/`) contain business logic (eligibility, search, notifications), Models (`model/`) define data shapes, Repositories (`repository/`) handle MongoDB persistence, and `config/` contains security and other configuration.

3) Why these technologies (short rationale)
- React (CRA): Fast developer iteration and interactive UI for search and forms; large ecosystem and simple dev server.
- Spring Boot: Strong typing (Java), mature ecosystem (Spring Security, Spring Data), robust production features and integration patterns (JWT, OAuth2, mail, scheduling). Chosen for maintainability and security-critical logic.
- MongoDB: Flexible document model for donors and users, and geospatial support for 'nearby donor' queries. Easy to run locally or via Atlas.

4) Key tradeoffs
- Pros: Strong backend structure and security, easy integration with Twilio/Email, good developer tooling for Java, React provides responsive UI.
- Cons: Mixed-language stack (Java + JavaScript) increases context switching; current in-memory search implementation (Haversine in Java) is simple but not scalable — prefer MongoDB 2dsphere queries for production.

5) Where to look in the repo
- Frontend: `frontend/src/index.js`, `frontend/src/App.js`, `frontend/src/context/AuthContext.js`, `frontend/src/components/*`.
- Backend: `backend/src/main/java/com/bloodbridge/controller/*`, `service/*`, `model/*`, `repository/*`, `config/*`.
- Build & deploy files: `frontend/Dockerfile`, `backend/Dockerfile`, `nginx.conf.template`.

6) Quick run (local dev)
Open two terminals:

```powershell
# Terminal 1: backend
Set-Location C:\Users\srika\Downloads\z2\backend
mvn spring-boot:run

# Terminal 2: frontend
Set-Location C:\Users\srika\Downloads\z2\frontend
npm install
npm start
```

7) Recommended next improvements
- Fix build/jdk/lombok compatibility (ensure annotation processing and matching Java version).  
- Replace in-memory distance filtering with MongoDB geospatial queries (GeoJSON `location` + `2dsphere` index).  
- Add input validation (`@Valid`) and a global exception handler for consistent error responses.  
- Move secrets (JWT secret, OAuth client IDs, Twilio keys) to environment variables or a secret manager.

8) Short notes on security
- The app uses JWTs (stateless authentication) and Spring Security. Frontend currently stores tokens in `localStorage` (common but consider `httpOnly` cookies for stronger XSS protection).

If you want, I can expand this file with diagrams, a small migration guide to switch donors to GeoJSON `location`, or a CI snippet to run tests and builds.
