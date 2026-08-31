# Banking Management System

A comprehensive, enterprise-grade banking management system built with Spring Boot, featuring secure authentication, account management, loan processing, transaction handling, and administrative controls.

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Architecture](#architecture)
- [Security](#security)
- [Database Schema](#database-schema)
- [Contributing](#contributing)

## Overview

The Banking Management System is a full-featured banking application designed to manage:
- Customer accounts and profiles
- Financial transactions (transfers, deposits, withdrawals)
- Loan management and approval workflows
- Beneficiary management
- Comprehensive audit logging
- Administrative dashboards and controls
- Role-based access control (RBAC)

This system provides a robust foundation for building modern banking solutions with security, scalability, and compliance in mind.

## Tech Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 4.1.0** - Framework for building Spring applications
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - ORM and database access
- **Hibernate** - JPA implementation

### Database
- **MySQL 8.0+** - Relational database

### Authentication & Security
- **JWT (JSON Web Token)** - Token-based authentication (jjwt v0.12.6)
- **BCrypt** - Password hashing and encoding
- **Spring Method Security** - Method-level authorization

### API Documentation
- **Springdoc OpenAPI 3.0.3** - API documentation and Swagger UI

### Development Tools
- **Lombok** - Boilerplate reduction (getters, setters, constructors)
- **Maven** - Dependency management and build tool

### Testing
- **Spring Boot Test** - Comprehensive testing framework

## Features

### 🔐 Authentication & Authorization
- User registration and login
- JWT-based token authentication
- Role-based access control (CUSTOMER, ADMIN)
- Secure password storage using BCrypt
- Custom user details service
- JWT authentication filter

### 👤 Customer Management
- Customer profile creation and management
- KYC (Know Your Customer) verification
- Personal information management (name, phone, address)
- Identity document tracking (Aadhaar, PAN)
- Customer dashboard

### 💳 Account Management
- Create and manage multiple accounts per customer
- Support for multiple account types:
  - Savings Account
  - Checking Account
  - Money Market Account
  - Certificate of Deposit
- Real-time balance tracking
- Account status management (ACTIVE, FROZEN, CLOSED)
- Account freeze functionality for admin
- IFSC code and account number generation

### 💰 Transaction Management
- Secure fund transfers between accounts
- Multiple transaction types:
  - Deposits
  - Withdrawals
  - Transfers
  - Bill Payments
- Transaction status tracking (PENDING, COMPLETED, FAILED)
- Unique reference number generation for each transaction
- Transaction history and statements
- Pagination support for transaction queries

### 🏦 Loan Management
- Loan application submission
- Multiple loan types:
  - Personal Loan
  - Home Loan
  - Auto Loan
  - Education Loan
- Loan approval/rejection workflow
- Automated EMI (Equated Monthly Installment) calculation
- Dynamic interest rate configuration
- Loan status tracking (PENDING, APPROVED, REJECTED, ACTIVE, CLOSED)
- Loan repayment tracking
- Customizable tenure and interest rates

### 👥 Beneficiary Management
- Add and manage trusted beneficiaries
- Beneficiary verification
- Account number and IFSC code validation
- Beneficiary status tracking (ACTIVE, INACTIVE, SUSPENDED)

### 📊 Admin Dashboard
- Comprehensive system monitoring
- User and account statistics
- Transaction analytics
- Loan portfolio overview
- System health monitoring

### 🔍 Audit & Compliance
- Comprehensive audit logging for all operations
- Track user actions (LOGIN, LOGOUT, CREATE, UPDATE, DELETE)
- Audit trail with timestamps
- Resource tracking (which entity was modified)
- IP address logging for security
- Success/failure tracking
- Admin audit report generation
- Compliance reporting

### 📈 Statement Management
- Generate account statements
- Transaction filtering and search
- Date range filtering
- Statement export functionality

## Project Structure

```
src/main/java/com/banking/banking_management_system/
├── BankingManagementSystemApplication.java
├── annotation/
│   └── Auditable.java                    # Custom audit annotation
├── aspect/
│   └── AuditAspect.java                  # AOP aspect for audit logging
├── config/
│   ├── JpaAuditConfig.java               # JPA audit configuration
│   ├── LoanInterestRateConfig.java       # Loan interest rates
│   └── SecurityConfig.java               # Spring Security configuration
├── controller/
│   ├── AccountController.java
│   ├── AdminAuditController.java
│   ├── AdminDashboardController.java
│   ├── AuthController.java
│   ├── BeneficiaryController.java
│   ├── CustomerController.java
│   ├── HealthController.java
│   ├── LoanController.java
│   ├── StatementController.java
│   ├── TestController.java
│   └── TransactionController.java
├── dto/
│   ├── request/                          # Request DTOs
│   └── response/                         # Response DTOs
├── entity/
│   ├── Account.java
│   ├── AuditLog.java
│   ├── BaseEntity.java
│   ├── Beneficiary.java
│   ├── Customer.java
│   ├── Loan.java
│   ├── Transaction.java
│   └── User.java
├── enums/
│   ├── AccountStatus.java
│   ├── AccountType.java
│   ├── AuditAction.java
│   ├── AuditResult.java
│   ├── BeneficiaryStatus.java
│   ├── LoanStatus.java
│   ├── LoanType.java
│   ├── NotificationType.java
│   ├── OTPPurpose.java
│   ├── Role.java
│   ├── TransactionStatus.java
│   ├── TransactionType.java
│   └── UserStatus.java
├── exception/
│   ├── AccountFrozenException.java
│   ├── DuplicateResourceException.java
│   ├── GlobalExceptionHandler.java
│   ├── InsufficientBalanceException.java
│   ├── InvalidCredentialsException.java
│   ├── InvalidTransactionException.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
├── mapper/
│   ├── AccountMapper.java
│   ├── AuditLogMapper.java
│   ├── BeneficiaryMapper.java
│   ├── CustomerMapper.java
│   ├── LoanMapper.java
│   └── TransactionMapper.java
├── repository/
│   ├── AccountRepository.java
│   ├── AuditLogRepository.java
│   ├── BeneficiaryRepository.java
│   ├── CustomerRepository.java
│   ├── LoanRepository.java
│   ├── TransactionRepository.java
│   └── UserRepository.java
├── security/
│   ├── handler/
│   │   ├── JwtAccessDeniedHandler.java
│   │   └── JwtAuthenticationEntryPoint.java
│   ├── jwt/
│   │   └── JwtAuthenticationFilter.java
│   └── service/
│       └── CustomUserDetailsService.java
└── service/
    ├── AccountService.java
    ├── AdminAccountService.java
    ├── AdminAuditService.java
    ├── AdminCustomerService.java
    ├── AdminDashboardService.java
    ├── AdminTransactionService.java
    ├── AuditService.java
    ├── AuthenticationService.java
    ├── BeneficiaryService.java
    ├── CustomerService.java
    ├── LoanCalculationService.java
    ├── LoanService.java
    ├── StatementService.java
    ├── TransactionService.java
    └── impl/                            # Service implementations
```

## Prerequisites

- **Java 17 or higher** - [Download Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- **MySQL 8.0+** - [Download MySQL](https://dev.mysql.com/downloads/mysql/)
- **Git** - Version control

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/olapuajay/Banking_Management_System.git
cd Banking_Management_System
```

### 2. Set Up Database

Create a MySQL database and user:

```sql
-- Create database
CREATE DATABASE banking_management_system;

-- Create user (optional, if not using root)
CREATE USER 'banking_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON banking_management_system.* TO 'banking_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/banking_management_system
spring.datasource.username=root
spring.datasource.password=root

# JWT Configuration
jwt.secret=your_secret_key_here_minimum_32_characters_recommended
jwt.expiration=3600000  # 1 hour in milliseconds

# Bank Configuration
bank.ifsc-code=BANK0001234

# Application Configuration
spring.application.name=banking-management-system
server.port=8080

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 4. Set Environment Variables

For JWT Secret (recommended for production):

```bash
export JWT_SECRET=your_very_secure_jwt_secret_key_minimum_32_characters
```

### 5. Build the Application

```bash
mvn clean install
```

Or using Maven wrapper:

```bash
./mvnw clean install
```

## Running the Application

### Development Mode

```bash
mvn spring-boot:run
```

Or:

```bash
./mvnw spring-boot:run
```

### Production Mode

```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/banking-management-system-0.0.1-SNAPSHOT.jar
```

### Verify Application is Running

The application will start on `http://localhost:8080`

Check health endpoint:
```bash
curl http://localhost:8080/api/v1/health
```

## API Documentation

Once the application is running, access the interactive API documentation:

### Swagger UI
- **URL**: `http://localhost:8080/swagger-ui.html`
- Interactive API explorer with try-it-out functionality
- Complete request/response schemas

### OpenAPI JSON
- **URL**: `http://localhost:8080/v3/api-docs`
- Machine-readable API specification

### Main API Endpoints

#### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login and get JWT token

#### Accounts
- `POST /api/v1/accounts` - Create new account
- `GET /api/v1/accounts/me` - Get my accounts
- `GET /api/v1/accounts/{accountId}` - Get account details (Admin)
- `PUT /api/v1/accounts/{accountId}/freeze` - Freeze account (Admin)

#### Transactions
- `POST /api/v1/transactions` - Create transaction
- `GET /api/v1/transactions/me` - Get my transactions
- `GET /api/v1/transactions/{transactionId}` - Get transaction details

#### Loans
- `POST /api/v1/loans` - Apply for loan
- `GET /api/v1/loans/me` - Get my loans
- `PUT /api/v1/loans/{loanId}/approve` - Approve loan (Admin)
- `PUT /api/v1/loans/{loanId}/repay` - Repay loan

#### Beneficiaries
- `POST /api/v1/beneficiaries` - Add beneficiary
- `GET /api/v1/beneficiaries` - List beneficiaries
- `DELETE /api/v1/beneficiaries/{beneficiaryId}` - Remove beneficiary

#### Admin
- `GET /api/v1/admin/dashboard` - Dashboard statistics
- `GET /api/v1/admin/audit-logs` - Audit logs
- `GET /api/v1/admin/customers` - Manage customers
- `GET /api/v1/admin/accounts` - Manage accounts

#### Statements
- `GET /api/v1/statements` - Generate account statement

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│     REST Controllers (HTTP API)     │
├─────────────────────────────────────┤
│     Service Layer (Business Logic)  │
├─────────────────────────────────────┤
│    Repository Layer (Data Access)   │
├─────────────────────────────────────┤
│   JPA/Hibernate (ORM Mapping)       │
├─────────────────────────────────────┤
│    MySQL Database                   │
└─────────────────────────────────────┘
```

### Key Components

#### Controllers
- Handle HTTP requests and responses
- Validate input using Jakarta validation annotations
- Return appropriate HTTP status codes
- Apply role-based authorization

#### Services
- Implement business logic
- Handle transactions and consistency
- Perform data validation
- Manage domain relationships

#### Repositories
- Use Spring Data JPA for data access
- Define custom queries for complex operations
- Provide pagination and sorting support

#### Mappers
- Convert between Entity and DTO objects
- Maintain separation between layers
- Handle object transformation logic

#### Security
- JWT token generation and validation
- Custom authentication provider
- Method-level authorization with @PreAuthorize
- Exception handlers for security events

#### Audit & Compliance
- AOP-based aspect for automatic audit logging
- Tracks all important operations
- Records user, action, resource, and result
- Maintains audit trail for compliance

## Security

### Authentication Flow

1. User registers with email and password
2. User logs in with credentials
3. System validates credentials and generates JWT token
4. JWT token is used for subsequent requests
5. Token is validated on each request via JwtAuthenticationFilter

### Password Security

- Passwords are hashed using BCrypt algorithm
- Never stored in plain text
- Salted hashing prevents rainbow table attacks

### Authorization

- Role-based access control (RBAC)
- Two roles: CUSTOMER and ADMIN
- Method-level authorization with @PreAuthorize annotations
- Custom permission checks in service layer

### Data Protection

- Account numbers are unique and indexed
- Transactions use reference numbers for audit trail
- Account balances use BigDecimal for precision
- Optimistic locking with @Version for concurrency control

### API Security

- CORS configuration can be customized
- CSRF protection enabled by default
- Rate limiting can be added
- Request validation with Jakarta Validation

## Database Schema

### Key Tables

#### Users
- User authentication and role information
- Email-based unique identification

#### Customers
- Customer profile information
- KYC verification status
- Link to User entity

#### Accounts
- Multiple accounts per customer
- Balance tracking with BigDecimal
- Account status management
- Indexed by account number for quick lookup

#### Transactions
- All financial transactions
- Source and destination account tracking
- Status and type management
- Indexed by reference number and account ID

#### Loans
- Loan applications and management
- Principal amount, interest rate, tenure
- Automatic EMI calculation
- Status tracking

#### Beneficiaries
- Trusted beneficiary accounts
- Verification status
- Unique constraint per customer

#### AuditLogs
- Complete audit trail
- Action tracking
- Resource and user tracking
- Indexed for efficient queries

### Database Indexes

Strategic indexes for performance:
- Account numbers
- Transaction reference numbers
- Loan numbers
- Audit logs (user, action, timestamp, resource)
- Customer IDs

## Contributing

Contributions are welcome! Please follow these guidelines:

### Development Workflow

1. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes**
   - Follow Java coding standards
   - Use meaningful commit messages
   - Add appropriate Javadoc comments

3. **Test your changes**
   ```bash
   mvn test
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Submit a pull request**
   - Include a clear description of changes
   - Reference any related issues
   - Ensure all tests pass

### Code Style

- Follow Google Java Style Guide
- Use Lombok annotations for reducing boilerplate
- Maintain separation of concerns
- Keep methods small and focused
- Use meaningful variable and method names

### Commit Message Format

```
type(scope): brief description

Detailed explanation of the changes made.

Closes #issue_number
```

Types: feat, fix, docs, style, refactor, test, chore

## License

[Add your license information here]

## Support

For issues, questions, or suggestions:
- Create an issue on GitHub
- Contact the development team
- Check existing documentation

## Authors

- **Ajay Olap** - Project Lead

---

**Last Updated**: 2026-08-31
**Current Version**: 0.0.1-SNAPSHOT
