# MediTrack — Healthcare Management & Reporting System

MediTrack is a portfolio project demonstrating Java/Spring Boot REST APIs, relational data modeling, SQL reporting, validation, error handling, and a React dashboard. Use fictional data only. This is an educational demo and is **not** suitable for real clinical or production use.

## Features

- Patient directory with create, read, update, delete, and name/email search.
- Appointment endpoints with status filtering and status updates.
- Summary reporting endpoints for patient and appointment metrics.
- Validation for patient fields and appointment status.
- Duplicate patient email protection and meaningful HTTP errors.
- Oracle-compatible DDL and sample analytical SQL queries.
- Responsive React dashboard with search, create-patient form, summary cards, and appointment overview.
- Basic Spring Boot context test.

## Technology stack

- Java 17, Spring Boot 3, Spring Web, Spring Data JPA, Bean Validation
- H2 in-memory database for quick local startup (Oracle compatibility mode)
- Oracle SQL scripts in `database/oracle/`
- React, Vite, Lucide icons

> The app runs on H2 by default so it can be tried without installing Oracle Database. The SQL scripts in `database/oracle/` are intended for Oracle Database and are not automatically executed by the demo app. Switching the application datasource to Oracle requires adding the Oracle JDBC driver and configuring credentials locally.

## Requirements

- Java 17+
- Maven 3.8+ (or use an IDE with Maven support)
- Node.js 18+ and npm

## Run locally

### 1. Start the backend

```bash
cd backend
mvn spring-boot:run
```

Backend base URL: `http://localhost:8080/api`

H2 console: `http://localhost:8080/h2-console`

Use JDBC URL `jdbc:h2:mem:meditrack`, username `sa`, and an empty password.

### 2. Start the frontend in another terminal

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

Open the Vite URL printed in the terminal (normally `http://localhost:5173`).

## REST API

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/patients` | List all patients; optional `?q=nameOrEmail` search |
| GET | `/api/patients/{id}` | Get one patient |
| POST | `/api/patients` | Create a patient |
| PUT | `/api/patients/{id}` | Update a patient |
| DELETE | `/api/patients/{id}` | Delete a patient |
| GET | `/api/appointments` | List appointments; optional `?status=SCHEDULED` or `?patientId=1` |
| POST | `/api/appointments` | Create an appointment with `patient: { "id": 1 }` |
| PATCH | `/api/appointments/{id}/status?status=COMPLETED` | Change appointment status |
| DELETE | `/api/appointments/{id}` | Delete an appointment |
| GET | `/api/reports/summary` | Dashboard totals |
| GET | `/api/reports/appointments-by-status` | Counts grouped by status |

### Example patient request

```json
{
  "fullName": "Aanya Sharma",
  "dateOfBirth": "1998-04-12",
  "gender": "FEMALE",
  "email": "aanya.demo@example.com",
  "phone": "9000000000",
  "notes": "Synthetic demo record"
}
```

### Example appointment request

Use a future `appointmentDateTime` in ISO format, for example:

```json
{
  "patient": { "id": 1 },
  "providerName": "Dr. Demo",
  "appointmentDateTime": "2030-04-20T10:30:00",
  "status": "SCHEDULED",
  "reason": "Routine check-up"
}
```

## Oracle SQL practice

1. Create a dedicated Oracle schema and run `database/oracle/schema.sql`.
2. Run queries from `database/oracle/reporting_queries.sql`.
3. Practice explaining joins, `GROUP BY`, `HAVING`, analytic functions, indexes, constraints, and query plans.

## Testing

```bash
cd backend
mvn test
```

## Publish to GitHub

Create an empty public repository on your GitHub account named `meditrack-healthcare-management` (do not add a README/license there if you keep this README). From this project directory run:

```bash
git init
git add .
git commit -m "Build MediTrack healthcare management and reporting app"
git branch -M main
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/meditrack-healthcare-management.git
git push -u origin main
```

Replace `YOUR_GITHUB_USERNAME` with your actual GitHub username. Review all files before making the repository public.

## Suggested next improvements

- Add appointment creation/editing UI and pagination.
- Add role-based authentication before handling any sensitive data.
- Add integration tests, API documentation with OpenAPI, and CI with GitHub Actions.
- Add Oracle JDBC configuration using environment variables, never commit database credentials.
- Add screenshots after running the application locally.

## Resume description (only after you run, test, and understand it)

**MediTrack — Healthcare Management & Reporting System** | Java, Spring Boot, SQL, React, JPA

- Built a healthcare management demo with REST APIs for patient records and appointment workflows using Java and Spring Boot.
- Designed relational tables and SQL reports using joins, aggregation, constraints, and analytic functions.
- Implemented validation, duplicate-record checks, error handling, and dashboard metrics for operational reporting.
