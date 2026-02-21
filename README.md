# LifePattern AI - Complete Documentation

<div align="center">

![LifePattern AI](https://img.shields.io/badge/LifePattern-AI-6366f1?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![React](https://img.shields.io/badge/React-18+-61DAFB?style=for-the-badge&logo=react&logoColor=black)

**A comprehensive platform for tracking daily habits, analyzing burnout risk, and optimizing well-being.**

[Quick Start](#-quick-start) • [API Documentation](#-api-documentation) • [Features](#-features) • [Architecture](#-architecture)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Configuration](#-configuration)
- [Development](#-development)
- [Production Deployment](#-production-deployment)
- [Testing](#-testing)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

**LifePattern AI** is a full-stack web application designed to help users:
- Track daily activities (sleep, work, study, entertainment)
- Monitor energy and stress levels
- Analyze burnout risk using AI-powered algorithms
- Visualize health trends over time
- Receive personalized wellness recommendations

### Key Highlights

✅ **RESTful API** - Clean, well-documented backend architecture  
✅ **JWT Authentication** - Secure user authentication and authorization  
✅ **Real-time Analytics** - Instant burnout risk calculation  
✅ **Trend Visualization** - Interactive charts with customizable date ranges  
✅ **Mobile Responsive** - Works seamlessly on all devices  
✅ **Docker Ready** - Easy deployment with Docker Compose  

---

## ✨ Features

### 🔐 Authentication & User Management
- User registration with email validation
- Secure login with JWT tokens (24-hour expiration)
- Profile management (update name & email)
- Password recovery (mock endpoint ready for email integration)

### 📊 Daily Activity Tracking
- Log multiple activity types: sleep, work, study, entertainment
- Track energy levels (1-10 scale)
- Monitor stress levels (1-10 scale)
- Add optional notes for context
- View, edit, and delete logs
- Automatic validation (total hours ≤ 24)

### 🧠 Burnout Analysis
- Real-time burnout score calculation (0-100)
- Risk level categorization (LOW, MEDIUM, HIGH)
- Personalized wellness suggestions
- Historical analysis tracking
- Regenerate analysis on demand

### 📈 Trend Analysis
- Interactive line charts (Recharts)
- Customizable date ranges (7 days, 30 days, custom)
- Sleep hours vs. Stress level visualization
- Downloadable data (future enhancement)

### ⚙️ Settings & Customization
- Update profile information
- Change email (with uniqueness validation)
- Update display name
- Account management

---

## 🛠 Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Programming language |
| Spring Boot | 3.2.2 | Application framework |
| Spring Security | 6.x | Authentication & Authorization |
| Spring Data JPA | 3.x | Database ORM |
| MySQL | 8.0 | Relational database |
| JWT (jjwt) | 0.12.3 | Token-based authentication |
| Lombok | Latest | Reduce boilerplate code |
| Maven | 3.6+ | Build & dependency management |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| React | 18+ | UI framework |
| TypeScript | Latest | Type-safe JavaScript |
| TailwindCSS | 3.x | Utility-first CSS |
| React Query | Latest | Server state management |
| Recharts | Latest | Data visualization |
| React Router | 6.x | Client-side routing |
| Axios | Latest | HTTP client |

### DevOps
| Technology | Purpose |
|------------|---------|
| Docker | Containerization |
| Docker Compose | Multi-container orchestration |
| Git | Version control |

---

## 🚀 Quick Start

### Prerequisites

Before you begin, ensure you have the following installed:

```bash
# Check Java version (must be 17+)
java -version

# Check Maven version (must be 3.6+)
mvn -version

# Check Docker version
docker --version
docker-compose --version

# Check Node.js version (for frontend)
node --version
npm --version
```

### Installation Steps

#### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/lifepattern-ai.git
cd lifepattern-ai
```

#### 2️⃣ Backend Setup

```bash
cd lifepattern-backend

# Start MySQL with Docker
docker-compose up -d

# Wait for MySQL to be ready (about 10 seconds)
# Check status
docker-compose ps

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080/api`

#### 3️⃣ Frontend Setup

```bash
cd ../lifepattern-frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend will start on `http://localhost:5173`

#### 4️⃣ Verify Installation

Open your browser and navigate to:
- Frontend: `http://localhost:5173`
- Backend Health: `http://localhost:8080/api/auth/me` (will return 401 - expected)

---

## 📁 Project Structure

### Backend Structure

```
lifepattern-backend/
├── src/
│   ├── main/
│   │   ├── java/com/lifepattern/ai/
│   │   │   ├── config/              # Configuration classes
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── AnalysisController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   └── LogsController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── AIAnalysisResponse.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── DailyLogRequest.java
│   │   │   │   ├── DailyLogResponse.java
│   │   │   │   ├── ForgotPasswordRequest.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   ├── TrendDataResponse.java
│   │   │   │   ├── UpdateProfileRequest.java
│   │   │   │   └── UserResponse.java
│   │   │   ├── entity/              # JPA Entities
│   │   │   │   ├── AIAnalysis.java
│   │   │   │   ├── DailyLog.java
│   │   │   │   └── User.java
│   │   │   ├── exception/           # Exception Handling
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/          # Data Access Layer
│   │   │   │   ├── AIAnalysisRepository.java
│   │   │   │   ├── DailyLogRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/            # Security Components
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── JwtService.java
│   │   │   ├── service/             # Business Logic
│   │   │   │   ├── AnalysisService.java
│   │   │   │   ├── AuthService.java
│   │   │   │   └── DailyLogService.java
│   │   │   └── LifePatternAiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                        # Unit & Integration Tests
├── docker-compose.yml               # Docker Compose configuration
├── init.sql                         # Database initialization
├── pom.xml                          # Maven dependencies
└── README.md
```

### Frontend Structure

```
lifepattern-frontend/
├── src/
│   ├── api/                         # API Client
│   │   └── client.ts
│   ├── components/                  # Shared Components
│   │   └── Layout.tsx
│   ├── modules/                     # Feature Modules
│   │   ├── auth/
│   │   │   ├── AuthContext.tsx
│   │   │   ├── LoginPage.tsx
│   │   │   ├── RegisterPage.tsx
│   │   │   └── ForgotPasswordPage.tsx
│   │   ├── dashboard/
│   │   │   └── DashboardPage.tsx
│   │   ├── logs/
│   │   │   ├── LogListPage.tsx
│   │   │   └── LogFormPage.tsx
│   │   └── settings/
│   │       └── SettingsPage.tsx
│   ├── types.ts                     # TypeScript types
│   ├── App.tsx                      # Main app component
│   └── index.tsx                    # Entry point
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

---

## 📡 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### 1. Register User
```http
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (201 Created):**
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

#### 2. Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
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

#### 3. Get Current User
```http
GET /auth/me
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "id": "1",
  "name": "John Doe",
  "email": "john@example.com"
}
```

#### 4. Update Profile
```http
PUT /auth/profile
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

**Response (200 OK):**
```json
{
  "id": "1",
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

#### 5. Forgot Password
```http
POST /auth/forgot-password
Content-Type: application/json

{
  "email": "john@example.com"
}
```

**Response (200 OK):**
```json
{
  "message": "If the email exists, a password reset link has been sent"
}
```

---

### Daily Logs Endpoints

#### 1. Get All Logs
```http
GET /logs
Authorization: Bearer {token}
```

**Response (200 OK):**
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
  }
]
```

#### 2. Create Log
```http
POST /logs
Authorization: Bearer {token}
Content-Type: application/json

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

**Response (201 Created):**
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

#### 3. Get Single Log
```http
GET /logs/{id}
Authorization: Bearer {token}
```

#### 4. Update Log
```http
PUT /logs/{id}
Authorization: Bearer {token}
Content-Type: application/json

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

#### 5. Delete Log
```http
DELETE /logs/{id}
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "message": "Log deleted successfully"
}
```

---

### Analysis Endpoints

#### 1. Get Latest Analysis
```http
GET /analysis/latest
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "userId": "1",
  "burnoutScore": 45,
  "riskLevel": "MEDIUM",
  "suggestionText": "You're showing moderate signs of stress. Consider reducing work hours and increasing sleep time.",
  "analyzedAt": "2024-02-21T10:30:00"
}
```

#### 2. Get Trends
```http
GET /analysis/trends?days=7
Authorization: Bearer {token}

# Or with custom date range
GET /analysis/trends?start=2024-02-01&end=2024-02-21
Authorization: Bearer {token}
```

**Response (200 OK):**
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
  }
]
```

#### 3. Regenerate Analysis
```http
POST /analysis/regenerate
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "userId": "1",
  "burnoutScore": 50,
  "riskLevel": "MEDIUM",
  "suggestionText": "Your work hours (8.0h) and stress level (6/10) are concerning...",
  "analyzedAt": "2024-02-21T14:45:00"
}
```

---

## 🗄 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email)
);
```

### Daily Logs Table
```sql
CREATE TABLE daily_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,
    sleep_hours DOUBLE NOT NULL,
    work_hours DOUBLE NOT NULL,
    study_hours DOUBLE NOT NULL,
    entertainment_hours DOUBLE NOT NULL,
    energy_level INT NOT NULL CHECK (energy_level BETWEEN 1 AND 10),
    stress_level INT NOT NULL CHECK (stress_level BETWEEN 1 AND 10),
    notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_date (user_id, date),
    INDEX idx_user_date (user_id, date)
);
```

### AI Analysis Table
```sql
CREATE TABLE ai_analysis (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    burnout_score INT NOT NULL CHECK (burnout_score BETWEEN 0 AND 100),
    risk_level VARCHAR(20) NOT NULL,
    suggestion_text TEXT NOT NULL,
    analyzed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_analyzed (user_id, analyzed_at)
);
```

---

## ⚙️ Configuration

### Backend Configuration

**File:** `src/main/resources/application.properties`

```properties
# Application
spring.application.name=lifepattern-ai-backend
server.port=8080
server.servlet.context-path=/api

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/lifepattern_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=lifepattern_user
spring.datasource.password=lifepattern_password_123

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT
jwt.secret=dGhpcy1pcy1hLXNlY3VyZS0yNTYtYml0LXNlY3JldC1rZXktZm9yLWp3dC10b2tlbi1nZW5lcmF0aW9uLWFuZC12YWxpZGF0aW9uLXByb2Nlc3M=
jwt.expiration=86400000

# CORS
cors.allowed-origins=http://localhost:5173,http://localhost:3000
```

### Frontend Configuration

**File:** `.env.local`

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

---

## 🔧 Development

### Running in Development Mode

**Backend:**
```bash
cd lifepattern-backend
mvn spring-boot:run

# With hot reload (Spring DevTools included)
# Changes to Java files will auto-reload
```

**Frontend:**
```bash
cd lifepattern-frontend
npm run dev

# Vite will auto-reload on file changes
```

### Building for Production

**Backend:**
```bash
mvn clean package
java -jar target/ai-backend-1.0.0.jar
```

**Frontend:**
```bash
npm run build
# Output in dist/ folder
```

---

## 🧪 Testing

### Backend Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=AuthServiceTest

# Run with coverage
mvn test jacoco:report
```

### Manual API Testing

**Using cURL:**
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"test123","name":"Test User"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"test123"}'
```

**Using Postman:**
- Import `LifePattern-AI-API.postman_collection.json`
- Set `{{baseUrl}}` variable to `http://localhost:8080/api`
- Set `{{token}}` variable after login

---

## 🚢 Production Deployment

### Environment Variables

Create `.env` file:

```env
# Database
DB_HOST=your-db-host
DB_PORT=3306
DB_NAME=lifepattern_db
DB_USER=lifepattern_user
DB_PASSWORD=your-secure-password

# JWT
JWT_SECRET=your-256-bit-base64-encoded-secret
JWT_EXPIRATION=86400000

# CORS
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com

# Server
SERVER_PORT=8080
```

### Security Checklist

- [ ] Change JWT secret to a strong, unique value
- [ ] Use strong database passwords
- [ ] Enable HTTPS/TLS
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Configure proper CORS origins
- [ ] Enable rate limiting
- [ ] Set up database backups
- [ ] Configure logging levels
- [ ] Use environment variables for secrets
- [ ] Enable SQL injection protection

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Port 3306 Already in Use
```bash
# Stop existing MySQL
docker-compose down

# Or change port in docker-compose.yml
ports:
  - "3307:3306"  # Use 3307 instead
```

#### 2. JWT Token Errors
```bash
# Regenerate secret key
echo -n "your-secret-key-at-least-256-bits-long" | base64

# Update in application.properties
jwt.secret=<generated-base64-string>
```

#### 3. CORS Errors
```properties
# Add your frontend URL to application.properties
cors.allowed-origins=http://localhost:5173,http://localhost:3000,https://yourdomain.com
```

#### 4. Database Connection Failed
```bash
# Check MySQL status
docker-compose ps

# View MySQL logs
docker-compose logs mysql

# Restart MySQL
docker-compose restart mysql
```

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [React Documentation](https://react.dev)
- [TailwindCSS](https://tailwindcss.com)
- [JWT.io](https://jwt.io)

---

## 👥 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Spring Boot Team
- React Community
- TailwindCSS Team
- All contributors and supporters

---

<div align="center">

**Built with ❤️ by the LifePattern AI Team**

[⬆ Back to Top](#lifepattern-ai---complete-documentation)

</div>