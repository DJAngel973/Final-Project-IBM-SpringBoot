# Questionnaire Application - Security & Thymeleaf Guide

## Project Overview

Web application developed with **Spring Boot** that implements a questionnaire system with user authentication and authorization. This project uses **Thymeleaf** as a template engine and **Spring Security** for access control.

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