# Questionnaire Application - Security & Thymeleaf Guide

## Project Overview

Web application developed with **Spring Boot** as part of a **Coursera IBM Java Spring Boot course project**. This application implements a questionnaire/quiz system with user authentication and authorization, demonstrating practical implementation of Spring Security and Thymeleaf template engine.

### Academic Context

This is a **learning project** developed as part of the IBM Java Spring Boot specialization on Coursera, focusing on:
- Spring Boot fundamentals
- Spring Security integration
- Server-side rendering with Thymeleaf
- RESTful architecture principles
- Database management with JPA

## Security Implementation

### Security Level: **BASIC-MEDIUM**

This project implements a **form-based authentication** security level with the following characteristics:

#### What it includes:
- User authentication with username and password
- Password encryption using BCrypt
- Role-based authorization (USER, ADMIN)
- CSRF protection for forms
- Session management
- Protection of specific routes

#### What it does NOT include:
- JWT Token generation
- OAuth2 authentication
- Multi-factor authentication (MFA)
- Advanced auditing

### Does it generate tokens?

**NO**, this implementation uses **session-based authentication**. When a user logs in:
1. Spring Security creates a session on the server
2. A session cookie (JSESSIONID) is sent to the browser
3. This cookie is used to maintain the session, not a JWT token

### What is a Token?

A **token** (specifically JWT - JSON Web Token) is an encoded string containing user information and permissions. It works differently than sessions:

**Sessions (this project):**
- State stored on the server
- Cookie with session ID
- Session validated on each request

**Tokens (JWT):**
- State stored on the client
- Token sent in Authorization header
- Token validated without querying the database

## Would security change with React instead of Thymeleaf?

**YES, it would change significantly:**

### With Thymeleaf (Current):
- Server-side rendering (SSR)
- Session-based authentication
- Forms handled by the server
- CSRF protection with hidden tokens

### With React (Frontend Separated):
- Client-side rendering (CSR)
- **JWT token authentication** would be needed
- REST API for communication
- Token stored in localStorage/sessionStorage
- CORS configuration required
- Stateless backend

**Example flow with React:**
1. User logs in → Backend returns JWT token
2. React stores the token
3. Every request includes: `Authorization: Bearer <token>`
4. Backend validates the token without sessions

## Project Structure

```plaintext
src/
├── main/
│   ├── java/com.app.questionnaire
│   │   ├── config
│   │   │   └── WebSecurityConfig.java
│   │   ├── controller
│   │   │   └── QuestionController.java
│   │   ├── model
│   │   │   ├── Question.java
│   │   │   ├── Role.java
│   │   │   └── User.java
│   │   ├── service
│   │   │   ├── QuestionsService.java
│   │   │   └── QuizUserDetailsService.java
│   │   └── QuestionnaireApplication.java
│   ├── resources
│   │   ├── static.css
│   │   │   └── home-landing.css
│   │   ├── templates
│   │   │   ├── add-questionnaire.html
│   │   │   ├── edit-questionnaire.html
│   │   │   ├── home.html
│   │   │   ├── home-landing.html
│   │   │   ├── login.html
│   │   │   ├── quizlist.html
│   │   │   ├── register.html
│   │   │   └── result.html
│   │   └── application.properties
└── test/
    └── java/com.app.questionnaire
        └── QuestionnaireApplicationTests

```