# Questionnaire

Quiz application built with Spring Boot. Admins create and manage questions. Users answer quizzes and get scored results. Form-based authentication with role-based access control.

## Quick Start

### Local Development

```bash
./mvnw spring-boot:run
```

Open `http://localhost:8080`. Register an **ADMIN** account to create questions, or a **USER** account to take the quiz.

### Docker Deployment (Recommended)

No need to install Java or Maven locally.

#### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed on your machine
- [Git](https://git-scm.com/downloads) (optional, for cloning)

#### Step 1: Get the Project

**Option A - Clone from GitHub:**
```bash
git clone https://github.com/DJAngel973/Final-Project-IBM-SpringBoot
cd questionnaire
```

**Option B - Download ZIP:**
1. Download the project ZIP from GitHub
2. Extract and navigate to the folder:
```bash
cd questionnaire
```

#### Step 2: Build the Docker Image

```bash
docker build -t questionnaire-app .
```

**What happens here:**
- Maven downloads dependencies and compiles the code
- Creates a lightweight production-ready image (~300MB)
- Multi-stage build ensures optimal size

#### Step 3: Run the Container

```bash
docker run -d -p 8080:8080 --name questionnaire questionnaire-app
```

**Command breakdown:**
- `-d` → Runs in background (detached mode)
- `-p 8080:8080` → Maps container port 8080 to your machine's port 8080
- `--name questionnaire` → Assigns a friendly name to the container

#### Step 4: Access the Application

Open your browser: **http://localhost:8080**

#### Step 5: Test the Application

1. **Create an Admin Account:**
   - Click "Register here"
   - Username: `admin`
   - Password: `tesTS$#gy123.`
   - Email: `admin@test.com`
   - Role: **Administrator**

2. **Create Questions (Admin):**
   - Login with admin credentials
   - You'll be redirected to Quiz List
   - Click "Add Questionnaire"
   - Enter question text and 4 answer options
   - Select the correct answer
   - Click "Add Question"

3. **Test as Regular User:**
   - Logout (top-right corner)
   - Register a new account with role **User**
   - Login → you'll be redirected to the quiz
   - Select your answers
   - Click "Submit Answers"
   - View your score and results

#### Managing the Container

```bash
# Check if container is running
docker ps

# View application logs
docker logs questionnaire

# View logs in real-time
docker logs -f questionnaire

# Stop the container
docker stop questionnaire

# Start the container again
docker start questionnaire

# Restart the container
docker restart questionnaire

# Remove the container (must stop first)
docker stop questionnaire
docker rm questionnaire

# Remove the image
docker rmi questionnaire-app
```

#### Troubleshooting

**Problem:** Port 8080 already in use
```bash
# Use a different port (e.g., 9090)
docker run -d -p 9090:8080 --name questionnaire questionnaire-app
# Then access via http://localhost:9090
```

**Problem:** Container won't start
```bash
# Check the logs for errors
docker logs questionnaire
```

**Problem:** Need to rebuild after code changes
```bash
docker stop questionnaire
docker rm questionnaire
docker rmi questionnaire-app
docker build -t questionnaire-app .
docker run -d -p 8080:8080 --name questionnaire questionnaire-app
```

#### Docker Compose (Alternative - Easier Management)

Create a `docker-compose.yml` file:

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    container_name: questionnaire
    restart: unless-stopped
```

Then use these simple commands:

```bash
# Start the application
docker-compose up -d

# Stop the application
docker-compose down

# View logs
docker-compose logs -f

# Rebuild and restart
docker-compose up -d --build
```

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
│   │   ├── Question.java                  # JPA entity with @ElementCollection
│   │   ├── Role.java                      # ADMIN, USER
│   │   └── User.java                      # JPA entity (table: app_user)
│   ├── repository/
│   │   ├── QuestionRepository.java        # Spring Data JPA repository
│   │   └── UserRepository.java            # findByUsername, existsByUsername
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
- Spring Security (form login, BCrypt, session-based auth)
- Spring Data JPA + Hibernate
- PostgreSQL 16
- Thymeleaf + Thymeleaf Spring Security extras
- Maven
- Docker (multi-stage build for production deployment)

No credentials are stored in the repository — `.env` is gitignored and `.env.example` serves as a template.

## CI/CD — GitHub Actions

This project uses **GitHub Actions** for continuous integration. The pipeline is defined in `.github/workflows/ci.yml` and runs automatically on every push and pull request to `main`.

### What the pipeline does

```
Push to main (or PR opened) → GitHub Actions runner starts
                                  │
                                  ▼
                            1. mvn clean package -DskipTests
                               Validates that the code compiles and all
                               Maven dependencies resolve correctly.
                                  │
                                  ▼
                            2. docker build -t questionnaire-app .
                               Validates that the multi-stage Dockerfile
                               produces a working image with the JAR and
                               non-root user.
                                  │
                              ✅ All green → PR can be merged
                              ❌ Any failure → Merge is blocked
```

### Why GitHub Actions instead of Jenkins?

| GitHub Actions | Jenkins |
|---|---|
| Zero infrastructure — no server to maintain | Requires a dedicated Jenkins server |
| Configuration lives in the repo (`.github/workflows/ci.yml`) | Configuration split between `Jenkinsfile` and server UI |
| Free and unlimited for public repositories | Server costs (hosting, maintenance) |
| Native integration with GitHub PRs and branch protection | Requires plugins for GitHub integration |
| Setup: push a YAML file and it works | Setup: install Jenkins, plugins, configure credentials |

For this MVP, GitHub Actions was the pragmatic choice — no server overhead, no plugin management, and the pipeline is version-controlled alongside the code. Jenkins remains a valid alternative for teams that already have a Jenkins infrastructure or need advanced pipeline orchestration beyond what Actions provides.

### Branch Protection

Branch protection rules are configured on `main`:

- **Require a pull request before merging** — prevents direct pushes to main
- **Require status checks: `build`** — blocks merge until the CI pipeline passes

This ensures that no broken code reaches `main` or gets deployed to Railway.

### The ci.yml file explained

```yaml
name: CI Pipeline                        # Display name in GitHub Actions tab

on:                                      # When does the pipeline run?
  push:
    branches: [main]                     # On every push to main
  pull_request:
    branches: [main]                     # On every PR targeting main

jobs:
  build:                                 # Single job named "build"
    runs-on: ubuntu-latest               # Free Linux runner from GitHub

    steps:
      - name: Checkout code              # Step 1: clone the repo
        uses: actions/checkout@v4

      - name: Set up JDK 21              # Step 2: install Java 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'        # Same JDK as Dockerfile & Railway
          cache: maven                   # Cache ~/.m2 for faster builds

      - name: Build with Maven           # Step 3: compile & package
        run: mvn clean package -DskipTests -B

      - name: Build Docker image         # Step 4: validate Dockerfile
        run: docker build -t questionnaire-app .
```

**Key details:**

- `-DskipTests` skips test execution since automated tests are planned for a future iteration. The flag can be removed once tests are implemented.
- `-B` (batch mode) suppresses interactive progress bars — required in non-interactive CI environments.
- `cache: maven` reuses the local Maven repository between runs, reducing build time from ~3 minutes to ~30 seconds on cache hits.
- The pipeline does NOT need a running PostgreSQL instance because `mvn package` compiles without starting the Spring context. Database connection happens at runtime (`java -jar`), not at build time.

## Deployment — Railway

The application is deployed on **Railway** (railway.app) with automatic deploys triggered by every push to `main`.

### Architecture on Railway

```
┌─────────────────────────────────────────────────────┐
│  Railway Project                                    │
│                                                     │
│  ┌──────────────────┐    ┌──────────────────────┐  │
│  │  app (Dockerfile) │───▶│  Postgres (managed)   │  │
│  │  Spring Boot      │    │  PostgreSQL 16        │  │
│  │  Port 8080        │    │  Persistent storage   │  │
│  └──────────────────┘    └──────────────────────┘  │
│          │                                               │
│     HTTPS via Let's Encrypt (automatic)                  │
│     https://<project>.up.railway.app                     │
└─────────────────────────────────────────────────────┘
```

### How it works

1. **GitHub connection** — Railway is linked to the GitHub repository. Every push to `main` triggers a new deploy automatically.

2. **Docker build** — Railway detects the `Dockerfile` in the repository root and executes the multi-stage build: Maven compiles the code, then the JAR is copied to a lightweight JRE Alpine image.

3. **Environment variables** — Railway's PostgreSQL service exposes variables like `PGHOST`, `PGPORT`, `PGUSER`, `PGPASSWORD`, and `PGDATABASE`. The app service references them using Railway's service reference syntax:

   ```
   SPRING_DATASOURCE_URL      = jdbc:postgresql://${{ Postgres.PGHOST }}:${{ Postgres.PGPORT }}/${{ Postgres.PGDATABASE }}
   SPRING_DATASOURCE_USERNAME = ${{ Postgres.PGUSER }}
   SPRING_DATASOURCE_PASSWORD = ${{ Postgres.PGPASSWORD }}
   ```

   Railway resolves `${{ Postgres.VARIABLE }}` at deploy time and injects the real values into the app container. Spring Boot reads them from `application.properties`.

   > **Why not use `DATABASE_URL` directly?** Railway's `DATABASE_URL` is in the format `postgresql://user:pass@host:5432/db`. Spring Boot's JDBC driver requires the prefix `jdbc:`. Using the individual components (`PGHOST`, `PGPORT`, `PGDATABASE`) allows constructing the correct JDBC URL.

4. **Database schema** — On first startup, Hibernate runs `ddl-auto=update` and automatically creates the `app_user`, `question`, and `question_options` tables in PostgreSQL. No manual SQL scripts are needed.

5. **HTTPS** — Railway provisions Let's Encrypt SSL certificates automatically. The application is served over HTTPS without any DNS or certificate configuration.

### Live URL

```
https://final-project-ibm-springboot-production.up.railway.app/
```

## License

This project is open source and available for educational purposes.

## Contributing

This is a Coursera final project, but contributions, issues, and feature requests are welcome for learning purposes!

## Support

If you have questions or need help running the project:
1. Check the Docker troubleshooting section above
2. Review the application logs: `docker logs questionnaire`
3. Open an issue on GitHub

## Author

Academic project demonstrating Spring Boot, Spring Security, Thymeleaf, JPA, PostgreSQL, Docker, GitHub Actions, and Railway deployment.
