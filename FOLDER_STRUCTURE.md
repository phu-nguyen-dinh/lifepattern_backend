# LifePattern AI - Project Structure Documentation

Complete guide to understanding the codebase organization and architecture.

## 📋 Table of Contents

- [Overview](#overview)
- [Backend Structure](#backend-structure)
- [Frontend Structure](#frontend-structure)
- [Design Patterns](#design-patterns)
- [Module Descriptions](#module-descriptions)
- [File Naming Conventions](#file-naming-conventions)

---

## Overview

LifePattern AI follows a **clean architecture** approach with clear separation of concerns:

```
lifepattern-ai/
├── lifepattern-backend/     # Spring Boot REST API
└── lifepattern-frontend/    # React SPA (if available)
```

---

## Backend Structure

### Complete Directory Tree

```
lifepattern-backend/
│
├── src/
│   ├── main/
│   │   ├── java/com/lifepattern/ai/
│   │   │   │
│   │   │   ├── config/                    # ⚙️ Configuration Layer
│   │   │   │   ├── CorsConfig.java        # CORS configuration
│   │   │   │   └── SecurityConfig.java    # Spring Security setup
│   │   │   │
│   │   │   ├── controller/                # 🌐 Presentation Layer
│   │   │   │   ├── AnalysisController.java   # /api/analysis/* endpoints
│   │   │   │   ├── AuthController.java       # /api/auth/* endpoints
│   │   │   │   └── LogsController.java       # /api/logs/* endpoints
│   │   │   │
│   │   │   ├── dto/                       # 📦 Data Transfer Objects
│   │   │   │   ├── Request DTOs:
│   │   │   │   │   ├── DailyLogRequest.java
│   │   │   │   │   ├── ForgotPasswordRequest.java
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   └── UpdateProfileRequest.java
│   │   │   │   │
│   │   │   │   └── Response DTOs:
│   │   │   │       ├── AIAnalysisResponse.java
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── DailyLogResponse.java
│   │   │   │       ├── TrendDataResponse.java
│   │   │   │       └── UserResponse.java
│   │   │   │
│   │   │   ├── entity/                    # 🗄️ Data Model Layer
│   │   │   │   ├── AIAnalysis.java        # Burnout analysis records
│   │   │   │   ├── DailyLog.java          # Daily activity logs
│   │   │   │   └── User.java              # User accounts
│   │   │   │
│   │   │   ├── exception/                 # ❌ Exception Handling
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   │
│   │   │   ├── repository/                # 💾 Data Access Layer
│   │   │   │   ├── AIAnalysisRepository.java
│   │   │   │   ├── DailyLogRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   │
│   │   │   ├── security/                  # 🔒 Security Components
│   │   │   │   ├── CustomUserDetailsService.java  # Load user for auth
│   │   │   │   ├── JwtAuthenticationFilter.java   # Request filter
│   │   │   │   └── JwtService.java                # JWT operations
│   │   │   │
│   │   │   ├── service/                   # 💼 Business Logic Layer
│   │   │   │   ├── AnalysisService.java   # Burnout calculation
│   │   │   │   ├── AuthService.java       # Authentication logic
│   │   │   │   └── DailyLogService.java   # Log management
│   │   │   │
│   │   │   └── LifePatternAiApplication.java  # 🚀 Application Entry Point
│   │   │
│   │   └── resources/
│   │       └── application.properties      # Application configuration
│   │
│   └── test/                              # 🧪 Test Suite
│       └── java/com/lifepattern/ai/
│           └── (Test classes mirror main structure)
│
├── docker-compose.yml                     # 🐳 Docker Compose config
├── init.sql                               # 📊 Database initialization
├── pom.xml                                # 📦 Maven dependencies
├── .gitignore                             # Git ignore rules
└── README.md                              # Project documentation
```

---

## Backend Layer Descriptions

### 1. Configuration Layer (`config/`)

**Purpose:** Application-wide configuration and setup

| File | Responsibility |
|------|----------------|
| `SecurityConfig.java` | Spring Security configuration, JWT setup, authentication provider |
| `CorsConfig.java` | Cross-Origin Resource Sharing configuration for frontend |

**Key Components:**
```java
// SecurityConfig.java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configures HTTP security
    // JWT authentication filter
    // Password encoder
    // Authentication manager
}

// CorsConfig.java
@Configuration
public class CorsConfig {
    // Allowed origins for CORS
    // Allowed methods (GET, POST, etc.)
    // Allowed headers
}
```

---

### 2. Controller Layer (`controller/`)

**Purpose:** Handle HTTP requests and responses (REST endpoints)

| File | Endpoints | Responsibility |
|------|-----------|----------------|
| `AuthController.java` | `/api/auth/*` | User authentication, registration, profile |
| `LogsController.java` | `/api/logs/*` | CRUD operations for daily logs |
| `AnalysisController.java` | `/api/analysis/*` | Burnout analysis and trends |

**Pattern:**
```java
@RestController
@RequestMapping("/endpoint")
@RequiredArgsConstructor
public class XController {
    private final XService service;
    
    @GetMapping
    public ResponseEntity<DTO> getMethod() {
        // Delegate to service
        return ResponseEntity.ok(result);
    }
    
    @PostMapping
    public ResponseEntity<DTO> postMethod(@Valid @RequestBody Request request) {
        // Validate, delegate, return
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
```

---

### 3. DTO Layer (`dto/`)

**Purpose:** Data transfer between layers, request/response models

**Request DTOs** (incoming data):
- Validation annotations
- No business logic
- Used in `@RequestBody`

**Response DTOs** (outgoing data):
- Clean API responses
- Hide sensitive data (e.g., passwords)
- Builder pattern for construction

**Example:**
```java
// Request DTO
@Data
public class LoginRequest {
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String password;
}

// Response DTO
@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    // No password!
}
```

---

### 4. Entity Layer (`entity/`)

**Purpose:** JPA entities mapping to database tables

| Entity | Table | Relationships |
|--------|-------|---------------|
| `User.java` | `users` | One-to-Many with DailyLog, AIAnalysis |
| `DailyLog.java` | `daily_logs` | Many-to-One with User |
| `AIAnalysis.java` | `ai_analysis` | Many-to-One with User |

**Pattern:**
```java
@Entity
@Table(name = "table_name")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    // Other fields with appropriate annotations
}
```

---

### 5. Repository Layer (`repository/`)

**Purpose:** Data access interface (Spring Data JPA)

**Capabilities:**
- Auto-generated queries from method names
- Custom queries with `@Query`
- No implementation needed (Spring Data magic!)

**Pattern:**
```java
@Repository
public interface XRepository extends JpaRepository<Entity, Long> {
    // Method name query
    Optional<Entity> findByEmail(String email);
    
    // Custom query
    @Query("SELECT e FROM Entity e WHERE ...")
    List<Entity> customQuery(@Param("param") Type param);
}
```

---

### 6. Service Layer (`service/`)

**Purpose:** Business logic and orchestration

| Service | Responsibility |
|---------|----------------|
| `AuthService.java` | User registration, login, profile updates |
| `DailyLogService.java` | Log CRUD, validation (24-hour rule) |
| `AnalysisService.java` | Burnout calculation, trend analysis |

**Pattern:**
```java
@Service
@RequiredArgsConstructor
public class XService {
    private final XRepository repository;
    
    @Transactional
    public ResponseDTO doSomething(RequestDTO request) {
        // 1. Validate
        // 2. Business logic
        // 3. Database operations
        // 4. Map to response DTO
        return responseDTO;
    }
}
```

---

### 7. Security Layer (`security/`)

**Purpose:** Authentication and authorization

| Component | Responsibility |
|-----------|----------------|
| `JwtService.java` | Generate/validate JWT tokens |
| `JwtAuthenticationFilter.java` | Intercept requests, validate tokens |
| `CustomUserDetailsService.java` | Load user for Spring Security |

**Flow:**
```
Request → JwtAuthenticationFilter → JwtService → UserDetailsService
                                   ↓
                              Set SecurityContext
                                   ↓
                              Controller (with Authentication)
```

---

### 8. Exception Layer (`exception/`)

**Purpose:** Centralized error handling

| Component | Responsibility |
|-----------|----------------|
| `GlobalExceptionHandler.java` | Catch exceptions, return proper HTTP responses |
| `BadRequestException.java` | 400 errors |
| `ResourceNotFoundException.java` | 404 errors |
| `ErrorResponse.java` | Standard error format |

**Pattern:**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        // Build error response
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
```

---

## Design Patterns Used

### 1. **MVC (Model-View-Controller)**
- Model: Entities
- View: DTOs (JSON responses)
- Controller: Controller layer

### 2. **Repository Pattern**
- Abstraction over data access
- Spring Data JPA repositories

### 3. **Service Layer Pattern**
- Business logic separation
- Transaction management

### 4. **DTO Pattern**
- Separate internal/external data models
- Hide implementation details

### 5. **Builder Pattern**
- Fluent object construction
- Used with Lombok `@Builder`

### 6. **Dependency Injection**
- Constructor injection with `@RequiredArgsConstructor`
- Loose coupling

### 7. **Filter Chain Pattern**
- JWT authentication filter
- Spring Security filter chain

---

## Request Flow Example

**Creating a Daily Log:**

```
1. Client sends POST /api/logs with JSON body
         ↓
2. JwtAuthenticationFilter validates token
         ↓
3. LogsController receives request
         ↓
4. @Valid validates DailyLogRequest DTO
         ↓
5. Controller calls DailyLogService.createLog()
         ↓
6. Service validates business rules (24-hour total)
         ↓
7. Service calls DailyLogRepository.save()
         ↓
8. Repository persists to database
         ↓
9. Service maps Entity → DailyLogResponse DTO
         ↓
10. Controller returns ResponseEntity with DTO
         ↓
11. Spring serializes to JSON
         ↓
12. Client receives response
```

---

## Frontend Structure (if available)

```
lifepattern-frontend/
│
├── src/
│   ├── api/                      # 🌐 API Client
│   │   └── client.ts             # Axios configuration
│   │
│   ├── components/               # 🧩 Shared Components
│   │   └── Layout.tsx            # App layout wrapper
│   │
│   ├── modules/                  # 📦 Feature Modules
│   │   ├── auth/                 # Authentication
│   │   │   ├── AuthContext.tsx  # Auth state management
│   │   │   ├── LoginPage.tsx
│   │   │   ├── RegisterPage.tsx
│   │   │   └── ForgotPasswordPage.tsx
│   │   │
│   │   ├── dashboard/            # Main dashboard
│   │   │   └── DashboardPage.tsx
│   │   │
│   │   ├── logs/                 # Daily logs
│   │   │   ├── LogListPage.tsx
│   │   │   └── LogFormPage.tsx
│   │   │
│   │   └── settings/             # User settings
│   │       └── SettingsPage.tsx
│   │
│   ├── types.ts                  # TypeScript types
│   ├── App.tsx                   # Main app component
│   └── index.tsx                 # Entry point
│
├── package.json                  # Dependencies
├── tsconfig.json                 # TypeScript config
├── vite.config.ts                # Vite config
└── README.md
```

---

## File Naming Conventions

### Backend (Java)

| Type | Convention | Example |
|------|------------|---------|
| Controller | `*Controller.java` | `AuthController.java` |
| Service | `*Service.java` | `AuthService.java` |
| Repository | `*Repository.java` | `UserRepository.java` |
| Entity | PascalCase | `DailyLog.java` |
| DTO Request | `*Request.java` | `LoginRequest.java` |
| DTO Response | `*Response.java` | `UserResponse.java` |
| Exception | `*Exception.java` | `BadRequestException.java` |
| Config | `*Config.java` | `SecurityConfig.java` |

### Frontend (TypeScript/React)

| Type | Convention | Example |
|------|------------|---------|
| Component | PascalCase + `.tsx` | `DashboardPage.tsx` |
| Context | `*Context.tsx` | `AuthContext.tsx` |
| Types | camelCase + `.ts` | `types.ts` |
| API Client | camelCase + `.ts` | `client.ts` |

---

## Database Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| Table | snake_case plural | `daily_logs` |
| Column | snake_case | `created_at` |
| Foreign Key | `{table}_id` | `user_id` |
| Index | `idx_{column}` | `idx_email` |
| Unique Constraint | `unique_{columns}` | `unique_user_date` |

---

## Module Dependencies

```
Controller Layer
      ↓
Service Layer
      ↓
Repository Layer
      ↓
Entity Layer
      ↓
Database
```

**Rule:** Higher layers depend on lower layers, never reverse!

**Security Filter** sits outside this hierarchy, intercepting requests before they reach controllers.

---

## Adding New Features

### Backend Example: Add "Mood Tracking"

1. **Create Entity** (`entity/Mood.java`)
2. **Create Repository** (`repository/MoodRepository.java`)
3. **Create DTOs** (`dto/MoodRequest.java`, `dto/MoodResponse.java`)
4. **Create Service** (`service/MoodService.java`)
5. **Create Controller** (`controller/MoodController.java`)
6. **Update Database** (JPA will auto-create table)
7. **Test** endpoints

### Frontend Example: Add "Mood Page"

1. **Create Page** (`modules/mood/MoodPage.tsx`)
2. **Add Route** in `App.tsx`
3. **Create API calls** in `api/client.ts`
4. **Add Navigation** in `Layout.tsx`

---

## Best Practices

### ✅ DO:
- Keep controllers thin (delegate to services)
- Put business logic in services
- Use DTOs for API boundaries
- Validate in DTOs with annotations
- Use `@Transactional` in services
- Handle exceptions globally
- Use meaningful variable names
- Add JavaDoc for public methods

### ❌ DON'T:
- Put business logic in controllers
- Expose entities directly in API responses
- Skip validation
- Catch exceptions in controllers (use `@ControllerAdvice`)
- Use `@Autowired` (use constructor injection)
- Return null (use `Optional`)
- Ignore error handling

---

## Code Organization Tips

1. **Group by feature** in services (not CRUD operations)
2. **Keep files focused** (Single Responsibility Principle)
3. **Use packages** for logical grouping
4. **Follow conventions** consistently
5. **Document complex logic** with comments
6. **Write tests** for critical paths

---

<div align="center">

**Project Structure Documentation v1.0**

[⬆ Back to Top](#lifepattern-ai---project-structure-documentation)

</div>