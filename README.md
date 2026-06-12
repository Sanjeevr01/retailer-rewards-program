# Retailer Rewards Program

## Overview

The Retailer Rewards Program is a Spring Boot REST API application that calculates reward points earned by customers based on their purchase transactions.

The application retrieves customer transactions from the database, calculates monthly reward points for the last three months, aggregates total reward points, and exposes the results through REST APIs.

---

# Business Requirement

A retailer wants to reward customers based on their spending.

Reward Rules:

* 2 points for every dollar spent over $100.
* 1 point for every dollar spent between $50 and $100.
* No reward points for purchases of $50 or less.

The application should:

* Calculate rewards for each transaction.
* Aggregate rewards monthly.
* Calculate total rewards for the last three months.
* Expose customer reward information through REST APIs.

---

# Reward Calculation Logic

### Rule 1

Transaction Amount <= $50

Reward Points = 0

Example:

Amount = $45

Points = 0

---

### Rule 2

Transaction Amount between $51 and $100

Reward Points = Amount - 50

Example:

Amount = $75

Points = 75 - 50 = 25

---

### Rule 3

Transaction Amount greater than $100

Reward Points =
(Amount - 100) × 2 + 50

Example:

Amount = $120

Points:

20 × 2 = 40

50 × 1 = 50

Total = 90

---

# Assumptions

The following assumptions were considered during development:

* Transactions are stored in an H2 in-memory database.
* Only transactions from the last three months are considered.
* Customer ID uniquely identifies a customer.
* All transactions belonging to a customer contain the same customer name.
* Reward points are calculated per transaction and aggregated monthly.
* Reward points are rounded to whole numbers.

---

# Technology Stack

| Technology      | Version            |
| --------------- | ------------------ |
| Java            | 17                 |
| Spring Boot     | 3.5.0              |
| Spring Data JPA | Latest             |
| Hibernate       | Latest             |
| H2 Database     | In-Memory          |
| Maven           | Build Tool         |
| Lombok          | Latest             |
| JUnit 5         | Testing            |
| Mockito         | Mocking            |
| MockMvc         | Controller Testing |

---

# Project Architecture

Application follows a layered architecture.

Client Request

↓

Controller Layer

↓

Service Layer

↓

Repository Layer

↓

H2 Database

### Layer Responsibilities

#### Controller

Handles incoming HTTP requests and returns API responses.

#### Service

Contains business logic for:

* Last 3 months filtering
* Reward calculation
* Monthly aggregation
* Total reward calculation

#### Repository

Interacts with the database using Spring Data JPA.

#### Database

Stores customer transaction information.

---

# Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.sanjeev.rewards
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── repository
│   │       ├── service
│   │       ├── util
│   │       └── RetailerRewardsApplication
│   │
│   └── resources
│
└── test
    └── java
        └── com.sanjeev.rewards
            ├── controller
            ├── repository
            ├── service
            └── RetailerRewardsApplicationTests
```

---

# Data Loading

The application uses a CommandLineRunner implementation to preload sample transaction data into the H2 database when the application starts.

Sample customers:

* John
* David
* Smith
* Michael
* Robert
* Emma
* Sophia
* William

More than 20 sample transactions are loaded automatically for testing and demonstration purposes.

---

# Last Three Months Logic

To satisfy the business requirement, only transactions from the last three months are considered.

Implementation:

```java
LocalDate threeMonthsAgo =
        LocalDate.now().minusMonths(3);
```

Repository Query:

```java
findByTransactionDateAfter(
        threeMonthsAgo
);
```

This ensures that reward calculations are performed only on recent transactions.

---

# API Endpoints

## 1. Get Rewards For All Customers

Request

```http
GET /api/rewards
```

Example

```text
http://localhost:8080/api/rewards
```

Response

```json
[
  {
    "customerId": 1,
    "customerName": "John",
    "monthlyRewards": [
      {
        "month": "2026-04",
        "rewardPoints": 90
      },
      {
        "month": "2026-05",
        "rewardPoints": 25
      }
    ],
    "totalRewards": 115
  }
]
```

---

## 2. Get Rewards By Customer ID

Request

```http
GET /api/rewards/{customerId}
```

Example

```text
http://localhost:8080/api/rewards/1
```

Response

```json
{
  "customerId": 1,
  "customerName": "John",
  "monthlyRewards": [
    {
      "month": "2026-04",
      "rewardPoints": 90
    },
    {
      "month": "2026-05",
      "rewardPoints": 25
    }
  ],
  "totalRewards": 115
}
```

---

# Exception Handling

The application uses global exception handling through:

```java
@RestControllerAdvice
```

### Customer Not Found

Response:

```json
{
  "timestamp": "2026-06-12T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with ID: 999"
}
```

### Internal Server Error

Response:

```json
{
  "timestamp": "2026-06-12T10:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Unexpected Error"
}
```

---

# Testing

The application contains:

## Unit Tests

Service Layer

* Reward Calculation Logic
* Customer Reward Retrieval
* Exception Scenarios
* Monthly Aggregation

Repository Layer

* Repository Query Validation

---

## Integration Tests

Controller Layer

* GET /api/rewards
* GET /api/rewards/{id}
* Error Responses

Service Layer

* End-to-End Reward Calculation
* Database Interaction Verification

---

# Test Coverage

Current Coverage:

* Controller Layer: 100%
* Service Layer: 100%
* Exception Layer: 100%
* Utility Layer: 100%
* Repository Layer: Tested
* Overall Coverage: ~98%

---

# Design Decisions

### Why Streams?

Java Streams were used for:

* Grouping transactions by customer
* Grouping transactions by month
* Summing reward points

Benefits:

* Cleaner code
* Better readability
* Reduced boilerplate
* Easier maintenance

---

### Why Double Instead of BigDecimal?

The coding assignment focuses on reward calculations rather than financial precision.

Double was chosen because:

* Simpler implementation
* Faster calculations
* Sufficient for assignment requirements

In production-grade financial systems, BigDecimal should be used to avoid floating-point precision issues.

---

# Running the Application

## Clone Repository

```bash
git clone <repository-url>
```

---

## Navigate To Project

```bash
cd retailer-rewards-program
```

---

## Build Project

```bash
mvn clean install
```

---

## Run Application

```bash
mvn spring-boot:run
```

---

# H2 Database Console

URL:

```text
http://localhost:8080/h2-console
```

Configuration:

```text
JDBC URL : jdbc:h2:mem:testdb
Username : sa
Password : 
```

---

# Future Enhancements

* Use BigDecimal for monetary calculations.
* Add pagination support.
* Add Swagger/OpenAPI documentation.
* Add Docker support.
* Add Authentication and Authorization.
* Store data in MySQL/PostgreSQL.
* Add caching using Redis.
* Add Kafka event publishing.

---

# Author

Sanjeev R
