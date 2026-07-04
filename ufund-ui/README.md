# UFund UI — Angular 18

Angular 18 standalone-component frontend for the UFund Spring Boot API.

## Prerequisites

| Tool | Version |
|------|---------|
| Node.js | 18 or higher |
| npm | 8 or higher |
| Angular CLI | 18 (`npm install -g @angular/cli@18`) |

## Quick start

```bash
# 1. Start the Spring Boot API first (from ufund-api/)
./mvnw spring-boot:run          # runs on http://localhost:8080

# 2. Install UI dependencies (ufund-ui/)
npm install

# 3. Serve the UI
npm start                       # opens http://localhost:4200
```

## Credentials

| Username | Password | Role |
|----------|----------|------|
| `admin`  | `admin`  | Admin — full CRUD via /admin |
| anything | anything | Helper — can browse, search, basket |

## Features

### Account state — persisted to localStorage
- Login / logout with role detection
- State survives browser refresh (username, role, basket, search history)
- Cleared on sign-out

### Search with saved history
- Debounced live search hitting `GET /needs?name=…`
- Last 5 unique search terms saved and shown as quick-access pills
- Last search term **automatically restored and re-executed** on page load

### Pages

| Route | Access | Description |
|-------|--------|-------------|
| `/needs` | Public | Cupboard — browse + search all needs |
| `/needs/:id` | Helper / Admin | Detail view — add/remove from basket |
| `/basket` | Helper / Admin | Basket — confirm funding |
| `/admin` | Admin only | Full CRUD: create, edit, delete needs |
| `/login` | Public | Sign in |

## Project structure

```
src/
├── app/
│   ├── models/
│   │   └── need.model.ts
│   ├── services/
│   │   ├── need.service.ts          # HTTP client for /needs API
│   │   └── account-state.service.ts # localStorage-backed signal state
│   ├── guards/
│   │   └── auth.guard.ts            # authGuard + adminGuard
│   ├── components/
│   │   ├── login/
│   │   ├── need-list/               # Search + cupboard grid
│   │   ├── need-detail/
│   │   ├── basket/
│   │   └── admin/                   # CRUD table
│   ├── app.component.ts             # Shell + nav
│   ├── app.config.ts
│   └── app.routes.ts
└── styles.css
```

## API

The UI expects the Spring Boot API at `http://localhost:8080`.
CORS is already configured in `WebConfig.java` (all origins allowed).
