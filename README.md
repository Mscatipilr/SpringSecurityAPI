# Spring Security API

This project is a simple Spring Boot-based REST API with JWT authentication. It provides user registration and login functionality with stateless JWT token-based authentication. The project also includes basic role-based access control (RBAC) for users and admins.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Running the Project](#running-the-project)
- [Testing the APIs](#testing-the-apis)
    - [Register a New User](#register-a-new-user)
    - [Log in and Generate a JWT Token](#log-in-and-generate-a-jwt-token)
    - [Access Secured Endpoints](#access-secured-endpoints)
    - [Access Admin Endpoints](#access-admin-endpoints)
- [Database](#database)
- [Environment Configuration](#environment-configuration)

## Prerequisites

Before running this project, ensure that you have the following tools installed:
- [Java 17](https://jdk.java.net/17/)
- [Maven](https://maven.apache.org/)
- [Postman](https://www.postman.com/) (or cURL) to test the API endpoints.

## Project Structure

```
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── org.joann.springsecurityapi
│   │   │   │   ├── controllers             # Contains API controllers
│   │   │   │   ├── models                  # Entity models (User, etc.)
│   │   │   │   ├── repositories            # JPA repositories
│   │   │   │   ├── security                # Security config and JWT utilities
│   │   │   │   ├── services                # Service layer for business logic
│   │   ├── resources
│   │   │   ├── application.properties      # Application configuration
├── pom.xml                                  # Maven dependencies
├── README.md                                # Project documentation
```


## Running the Project

To run the project locally, follow these steps:

1. **Clone the repository** (or download the source code):
   ```bash
   git clone https://github.com/your-repo/spring-security-api.git
   cd spring-security-api

## Testing the APIs

You can use Postman or cURL to test the API endpoints. Below are examples of common requests to register users, log in, and access protected resources.

### 1. Register a New User

- **Endpoint**: `POST /api/register`
- **Description**: Registers a new user in the system.
- **Request Body**:
  ```json
  {
    "username": "bobby",
    "password": "123",
    "role": "USER"
  }


### 2. Login

- **Endpoint**: `POST /api/login`
- **Description**: Login as the user you just registered.
- **Request Body**:
  ```json
  {
    "username": "bobby",
    "password": "123"
  }

Expected response:
```json
{
  "token": "<JWT_TOKEN>"
}
```