# 🍿 PopcornPing

**Never miss a movie release again.** PopcornPing is a Spring Boot backend that tracks your movie watchlist, syncs release dates daily from TMDB, and automatically reminds you via email and Google Calendar the moment your most-anticipated films drop.

---

## ✨ Features

- **🔐 Secure Authentication** — Email/password registration with OTP-based email verification and JWT-based stateless auth.
- **🎬 TMDB Integration** — Search, browse popular/upcoming movies, and pull rich movie metadata directly from The Movie Database API.
- **📋 Watchlist Management** — Add movies to a personal watchlist, track watch status (`WANT_TO_WATCH` / `WATCHED`), and remove or update entries.
- **📅 Google Calendar Integration** — Full OAuth2 authorization flow creates a calendar event on a movie's release day, directly on the user's own calendar, with automatic refresh-token handling for long-lived access.
- **🔄 Automated Daily Sync** — A scheduled job checks TMDB every night for release date changes, updates the database, recreates calendar events, and resets notification state — all wrapped in isolated, fault-tolerant transactions so one bad record never blocks the rest of the batch.
- **📧 Smart Email Notifications** — Automated reminder emails the night before a release, plus instant "release date changed" alerts when a studio shifts a date.
- **🛡️ Role-Based Admin Tools** — Admin-only endpoints to manually trigger sync/notification jobs and browse the movie database in a paginated view.

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3, Spring Security, Spring Data JPA |
| Database | MySQL |
| Auth | JWT, BCrypt, OAuth2 (Google) |
| External APIs | TMDB API, Google Calendar API |
| Email | Spring Mail (SMTP) |
| Scheduling | Spring `@Scheduled` |

---

## ⚙️ Architecture Highlights

- **Layered design** — clean separation between controllers, services, repositories, and mapping layers.
- **Resilient batch jobs** — per-record transaction boundaries (via a dedicated sync service) ensure a single TMDB failure or email bounce doesn't abort an entire nightly run.
- **OAuth2 done right** — proper authorization code flow with `access_type=offline` + `prompt=consent` to guarantee refresh tokens, and silent access-token refresh before every Calendar API call.
- **DTO-driven API surface** — entities are never exposed directly; every endpoint returns purpose-built response DTOs.

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- MySQL
- A TMDB API key
- A Google Cloud OAuth2 client (Calendar API enabled)
- An SMTP-enabled email account

### Environment Variables
```properties
DB_URL=
DB_USERNAME=
DB_PASSWORD=
SECRET_KEY=
SMTP_MAIL=
SMTP_MAIL_PASSWORD=
TMDB_API_TOKEN=
TMDB_BASE_URL=
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
```

### Run
```bash
git clone https://github.com/abhiraj-21/PopcornPing.git
cd PopcornPing
./mvnw spring-boot:run
```

---

## 📡 API Overview

| Endpoint | Description |
|---|---|
| `POST /api/auth/register` | Register a new user |
| `POST /api/auth/verify` | Verify account via OTP |
| `POST /api/auth/login` | Authenticate and receive JWT |
| `GET /api/movies/popular` | Browse popular movies |
| `GET /api/movies/search` | Search movies by title |
| `POST /api/watchlist/{tmdbId}` | Add a movie to your watchlist |
| `GET /api/calendar/auth` | Get Google Calendar consent URL |
| `GET /api/calendar/callback` | OAuth2 callback (token exchange) |


---

## 📄 License

MIT