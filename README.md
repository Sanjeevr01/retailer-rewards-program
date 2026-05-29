# Retailer Rewards Program

## Overview

This project is a Spring Boot REST API application that calculates reward points for customers based on their purchase transactions.

Customers earn:

* 2 points for every dollar spent over $100
* 1 point for every dollar spent between $50 and $100

Example:

* A purchase of $120 earns:

    * 2 × 20 = 40 points
    * 1 × 50 = 50 points
    * Total = 90 points

The application calculates:

* Monthly reward points per customer
* Total reward points across all months

---

# Technologies Used

* Java 17
* Spring Boot 3.5.0
* Spring Data JPA
* Hibernate
* H2 In-Memory Database
* Maven
* JUnit 5
* Mockito
* MockMvc
* Lombok

---

# Project Structure

```text
src
 ├── main
 │   ├── java/com/sanjeev/rewards
 │   │   ├── controller
 │   │   ├── dto
 │   │   ├── entity
 │   │   ├── exception
 │   │   ├── repository
 │   │   ├── service
 │   │   └── util
 │   └── resources
 │
 └── test
     ├── controller
     └── service
```

---

# Reward Calculation Logic

* Purchases below or equal to $50 → 0 points
* Purchases between $51 and $100 → 1 point per dollar over $50
* Purchases above $100:

    * 1 point per dollar between $50 and $100
    * 2 points per dollar above $100

---

# API Endpoints

## Get All Customer Rewards

```http
GET /api/rewards
```

Example:

```text
http://localhost:8080/api/rewards
```

---

## Get Rewards By Customer ID

```http
GET /api/rewards/{customerId}
```

Example:

```text
http://localhost:8080/api/rewards/1
```

---

# Sample Response

```json
{
  "customerId": 1,
  "customerName": "John",
  "monthlyRewards": [
    {
      "month": "2026-03",
      "points": 90
    },
    {
      "month": "2026-04",
      "points": 25
    }
  ],
  "totalRewards": 115
}
```

---

# Exception Handling

The application includes global exception handling using:

* `@RestControllerAdvice`
* Custom exception classes

Example:

* Customer not found → HTTP 404 response

---

# Testing

The application includes:

* Unit tests for service layer
* Integration tests for controller layer

Testing tools used:

* JUnit 5
* Mockito
* MockMvc

---

# Running the Application

## Clone Repository

```bash
git clone <repository-url>
```

---

## Navigate to Project

```bash
cd retailer-rewards
```

---

## Run Application

```bash
mvn spring-boot:run
```

---

# H2 Database Console

```text
http://localhost:8080/h2-console
```

Example configuration:

* JDBC URL: `jdbc:h2:mem:testdb`
* Username: `sa`
* Password: (empty)

---

# Author

Sanjeev
