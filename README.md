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
- Spring Security (form login, BCrypt, session-based auth)
- Thymeleaf + Thymeleaf Spring Security extras
- Maven
- In-memory storage
- Docker (multi-stage build for production deployment)

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

Academic project demonstrating Spring Boot, Spring Security, Thymeleaf, Docker.