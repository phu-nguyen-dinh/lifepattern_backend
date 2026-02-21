# LifePattern AI - Complete API Reference

## 📋 Table of Contents

- [Overview](#overview)
- [Authentication](#authentication)
- [Base URL](#base-url)
- [Request/Response Format](#requestresponse-format)
- [Error Handling](#error-handling)
- [Rate Limiting](#rate-limiting)
- [Endpoints](#endpoints)
  - [Auth Endpoints](#auth-endpoints)
  - [Logs Endpoints](#logs-endpoints)
  - [Analysis Endpoints](#analysis-endpoints)

---

## Overview

The LifePattern AI API is a RESTful service that provides endpoints for user authentication, daily activity logging, and burnout analysis.

### Key Features

✅ JWT-based authentication  
✅ RESTful design principles  
✅ JSON request/response format  
✅ Comprehensive error messages  
✅ Input validation  
✅ CORS support  

---

## Authentication

All protected endpoints require a JWT (JSON Web Token) in the `Authorization` header:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Getting a Token

Tokens are obtained through:
1. **Register** - `POST /api/auth/register`
2. **Login** - `POST /api/auth/login`

Tokens expire after 24 hours (configurable).

---

## Base URL

```
Development: http://localhost:8080/api
Production: https://api.yourdomain.com/api
```

---

## Request/Response Format

### Request Headers

```http
Content-Type: application/json
Authorization: Bearer {token}  // For protected endpoints
```

### Response Format

**Success Response:**
```json
{
  "id": "1",
  "name": "Data",
  "status": "success"
}
```

**Error Response:**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Detailed error message"
}
```

---

## Error Handling

### HTTP Status Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Invalid request data |
| 401 | Unauthorized | Missing or invalid authentication |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource conflict (e.g., duplicate email) |
| 500 | Internal Server Error | Server error |

### Error Response Examples

**Validation Error (400):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "email": "Email should be valid",
    "password": "Password must be at least 6 characters"
  }
}
```

**Unauthorized (401):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password"
}
```

**Not Found (404):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Daily log not found with id: 123"
}
```

---

## Rate Limiting

Currently no rate limiting is enforced. For production:
- Consider implementing rate limiting per IP/user
- Recommended: 100 requests per minute per user

---

## Endpoints

---

## Auth Endpoints

### 1. Register User

Create a new user account.

**Endpoint:** `POST /auth/register`

**Authentication:** None (Public)

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Validation Rules:**
- `name`: Required, 2-100 characters
- `email`: Required, valid email format, unique
- `password`: Required, minimum 6 characters

**Success Response (201 Created):**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
  "user": {
    "id": "1",
    "name": "John Doe",
    "email": "john@example.com"
  }
}
```

**Error Responses:**

- **400 Bad Request** - Email already exists
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email already exists"
}
```

- **400 Bad Request** - Validation failed
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "password": "Password must be at least 6 characters"
  }
}
```

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

---

### 2. Login

Authenticate existing user.

**Endpoint:** `POST /auth/login`

**Authentication:** None (Public)

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Success Response (200 OK):**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "1",
    "name": "John Doe",
    "email": "john@example.com"
  }
}
```

**Error Response (401 Unauthorized):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password"
}
```

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

---

### 3. Get Current User

Retrieve authenticated user information.

**Endpoint:** `GET /auth/me`

**Authentication:** Required (JWT)

**Request Headers:**
```http
Authorization: Bearer {token}
```

**Success Response (200 OK):**
```json
{
  "id": "1",
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Error Response (401 Unauthorized):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid token"
}
```

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

### 4. Update Profile

Update user name and/or email.

**Endpoint:** `PUT /auth/profile`

**Authentication:** Required (JWT)

**Request Headers:**
```http
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

**Validation Rules:**
- `name`: Required, 2-100 characters
- `email`: Required, valid email format, unique (if changing)

**Success Response (200 OK):**
```json
{
  "id": "1",
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

**Error Responses:**

- **400 Bad Request** - Email already exists
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email already exists"
}
```

**Example cURL:**
```bash
curl -X PUT http://localhost:8080/api/auth/profile \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com"
  }'
```

---

### 5. Forgot Password

Initiate password reset (mock implementation).

**Endpoint:** `POST /auth/forgot-password`

**Authentication:** None (Public)

**Request Body:**
```json
{
  "email": "john@example.com"
}
```

**Success Response (200 OK):**
```json
{
  "message": "If the email exists, a password reset link has been sent"
}
```

**Note:** This is a mock endpoint. In production, integrate with email service.

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com"
  }'
```

---

## Logs Endpoints

### 1. Get All Logs

Retrieve all daily logs for authenticated user.

**Endpoint:** `GET /logs`

**Authentication:** Required (JWT)

**Query Parameters:** None

**Success Response (200 OK):**
```json
[
  {
    "id": "1",
    "date": "2024-02-21",
    "sleepHours": 7.5,
    "workHours": 8.0,
    "studyHours": 2.0,
    "entertainmentHours": 3.0,
    "energyLevel": 7,
    "stressLevel": 5,
    "notes": "Productive day"
  },
  {
    "id": "2",
    "date": "2024-02-20",
    "sleepHours": 6.0,
    "workHours": 9.0,
    "studyHours": 1.0,
    "entertainmentHours": 2.5,
    "energyLevel": 5,
    "stressLevel": 7,
    "notes": "Long work day"
  }
]
```

**Notes:**
- Results are sorted by date descending (newest first)
- Returns empty array if no logs exist

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/logs \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

### 2. Create Log

Create a new daily log entry.

**Endpoint:** `POST /logs`

**Authentication:** Required (JWT)

**Request Body:**
```json
{
  "date": "2024-02-21",
  "sleepHours": 7.5,
  "workHours": 8.0,
  "studyHours": 2.0,
  "entertainmentHours": 3.0,
  "energyLevel": 7,
  "stressLevel": 5,
  "notes": "Productive day"
}
```

**Validation Rules:**
- `date`: Required, valid date format
- `sleepHours`: Required, 0-24
- `workHours`: Required, 0-24
- `studyHours`: Required, 0-24
- `entertainmentHours`: Required, 0-24
- **Total hours ≤ 24** (sleep + work + study + entertainment)
- `energyLevel`: Required, 1-10
- `stressLevel`: Required, 1-10
- `notes`: Optional, text

**Success Response (201 Created):**
```json
{
  "id": "1",
  "date": "2024-02-21",
  "sleepHours": 7.5,
  "workHours": 8.0,
  "studyHours": 2.0,
  "entertainmentHours": 3.0,
  "energyLevel": 7,
  "stressLevel": 5,
  "notes": "Productive day"
}
```

**Error Responses:**

- **400 Bad Request** - Total hours exceed 24
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Total hours cannot exceed 24 hours per day"
}
```

- **400 Bad Request** - Duplicate date
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "A log already exists for this date"
}
```

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/logs \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2024-02-21",
    "sleepHours": 7.5,
    "workHours": 8.0,
    "studyHours": 2.0,
    "entertainmentHours": 3.0,
    "energyLevel": 7,
    "stressLevel": 5,
    "notes": "Productive day"
  }'
```

---

### 3. Get Single Log

Retrieve a specific daily log by ID.

**Endpoint:** `GET /logs/{id}`

**Authentication:** Required (JWT)

**Path Parameters:**
- `id`: Log ID (integer)

**Success Response (200 OK):**
```json
{
  "id": "1",
  "date": "2024-02-21",
  "sleepHours": 7.5,
  "workHours": 8.0,
  "studyHours": 2.0,
  "entertainmentHours": 3.0,
  "energyLevel": 7,
  "stressLevel": 5,
  "notes": "Productive day"
}
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Daily log not found with id: 1"
}
```

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/logs/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

### 4. Update Log

Update an existing daily log.

**Endpoint:** `PUT /logs/{id}`

**Authentication:** Required (JWT)

**Path Parameters:**
- `id`: Log ID (integer)

**Request Body:**
```json
{
  "date": "2024-02-21",
  "sleepHours": 8.0,
  "workHours": 7.0,
  "studyHours": 2.0,
  "entertainmentHours": 4.0,
  "energyLevel": 8,
  "stressLevel": 4,
  "notes": "Better day"
}
```

**Validation Rules:** Same as Create Log

**Success Response (200 OK):**
```json
{
  "id": "1",
  "date": "2024-02-21",
  "sleepHours": 8.0,
  "workHours": 7.0,
  "studyHours": 2.0,
  "entertainmentHours": 4.0,
  "energyLevel": 8,
  "stressLevel": 4,
  "notes": "Better day"
}
```

**Error Responses:**

- **404 Not Found** - Log doesn't exist
- **400 Bad Request** - Validation errors (same as Create)

**Example cURL:**
```bash
curl -X PUT http://localhost:8080/api/logs/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2024-02-21",
    "sleepHours": 8.0,
    "workHours": 7.0,
    "studyHours": 2.0,
    "entertainmentHours": 4.0,
    "energyLevel": 8,
    "stressLevel": 4,
    "notes": "Better day"
  }'
```

---

### 5. Delete Log

Delete a daily log entry.

**Endpoint:** `DELETE /logs/{id}`

**Authentication:** Required (JWT)

**Path Parameters:**
- `id`: Log ID (integer)

**Success Response (200 OK):**
```json
{
  "message": "Log deleted successfully"
}
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Daily log not found with id: 1"
}
```

**Example cURL:**
```bash
curl -X DELETE http://localhost:8080/api/logs/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## Analysis Endpoints

### 1. Get Latest Analysis

Retrieve the most recent burnout analysis.

**Endpoint:** `GET /analysis/latest`

**Authentication:** Required (JWT)

**Success Response (200 OK):**
```json
{
  "userId": "1",
  "burnoutScore": 45,
  "riskLevel": "MEDIUM",
  "suggestionText": "You're showing moderate signs of stress. Consider reducing work hours (8.0h currently) and increasing sleep time. Try relaxation techniques and ensure you're taking regular breaks.",
  "analyzedAt": "2024-02-21T10:30:00"
}
```

**Risk Levels:**
- `LOW`: Burnout score < 40
- `MEDIUM`: Burnout score 40-69
- `HIGH`: Burnout score ≥ 70

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "No analysis found. Please create a daily log first."
}
```

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/analysis/latest \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

### 2. Get Trends

Retrieve health trends over time.

**Endpoint:** `GET /analysis/trends`

**Authentication:** Required (JWT)

**Query Parameters:**

**Option 1: By number of days**
```
?days=7    // Last 7 days
?days=30   // Last 30 days
```

**Option 2: By date range**
```
?start=2024-02-01&end=2024-02-21
```

**Success Response (200 OK):**
```json
[
  {
    "date": "2024-02-21",
    "sleep": 7.5,
    "stress": 5
  },
  {
    "date": "2024-02-20",
    "sleep": 6.0,
    "stress": 7
  },
  {
    "date": "2024-02-19",
    "sleep": 8.0,
    "stress": 4
  }
]
```

**Notes:**
- Results are sorted by date ascending (oldest first)
- Returns only logs within the specified range
- Returns empty array if no logs in range

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Please provide either 'days' or both 'start' and 'end' dates"
}
```

**Example cURL:**
```bash
# Last 7 days
curl -X GET "http://localhost:8080/api/analysis/trends?days=7" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Custom date range
curl -X GET "http://localhost:8080/api/analysis/trends?start=2024-02-01&end=2024-02-21" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

### 3. Regenerate Analysis

Generate a new burnout analysis based on latest log.

**Endpoint:** `POST /analysis/regenerate`

**Authentication:** Required (JWT)

**Request Body:** None

**Success Response (200 OK):**
```json
{
  "userId": "1",
  "burnoutScore": 50,
  "riskLevel": "MEDIUM",
  "suggestionText": "You're showing moderate signs of stress. Consider reducing work hours (8.0h currently) and increasing sleep time. Try relaxation techniques and ensure you're taking regular breaks.",
  "analyzedAt": "2024-02-21T14:45:00"
}
```

**Burnout Calculation Formula:**
```
score = (workHours × 5) + (stressLevel × 5) - (sleepHours × 3)
Clamped between 0-100
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2024-02-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "No daily logs found. Please create a log first."
}
```

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/analysis/regenerate \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## Postman Collection

Import the provided Postman collection for easy testing:

**File:** `LifePattern-AI-API.postman_collection.json`

**Variables to set:**
- `baseUrl`: `http://localhost:8080/api`
- `token`: Your JWT token (auto-set after login)

---

## API Changelog

### Version 1.0.0 (Current)
- Initial release
- Auth endpoints (register, login, profile)
- CRUD operations for daily logs
- Burnout analysis and trends
- JWT authentication

---

## Support

For issues or questions:
- GitHub Issues: [Create an issue](https://github.com/yourusername/lifepattern-ai/issues)
- Email: support@lifepattern.ai

---

<div align="center">

**API Documentation v1.0.0**

[⬆ Back to Top](#lifepattern-ai---complete-api-reference)

</div>