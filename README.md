# Questionnaire

Quiz application built with Spring Boot. Admins create and manage questions. Users answer quizzes and get scored results. Form-based authentication with role-based access control.

## Quick Start

```bash
./mvnw spring-boot:run
```

Open `http://localhost:8080`. Register an **ADMIN** account to create questions, or a **USER** account to take the quiz.

## Endpoints & Roles

| URL | Access | Description |
|---|---|---|
| `/` | Public | Landing page |
| `/login` | Public | Login form |
| `/register` | Public | User registration |
| `/quizlist/**` | ADMIN | Manage questions (CRUD) |
| `/quiz` | USER | Answer the quiz |
| `/quiz/results` | USER | View score and results |

After login, users are redirected based on role: ADMIN → `/quizlist`, USER → `/quiz`.

## Project Structure

```
src/
├── main/java/com/app/questionnaire/
│   ├── config/
│   │   └── WebSecurityConfig.java         # Security filter chain & BCrypt
│   ├── controller/
│   │   └── QuestionController.java        # All endpoints
│   ├── model/
│   │   ├── Question.java
│   │   ├── Role.java                      # ADMIN, USER
│   │   └── User.java
│   ├── service/
│   │   ├── QuestionsService.java          # Question CRUD & answer validation
│   │   └── QuizUserDetailsService.java    # UserDetailsService & registration
│   └── QuestionnaireApplication.java
├── main/resources/
│   ├── static/css/                        # Per-page stylesheets
│   ├── templates/                         # Thymeleaf views
│   └── application.properties
└── test/
    └── QuestionnaireApplicationTests.java
```

## Tech Stack

- Java 21
- Spring Boot 3.4.2
- Spring Security 6 (form login, BCrypt, session-based auth)
- Thymeleaf + Thymeleaf Spring Security extras
- Maven
- In-memory storage (no external database required)