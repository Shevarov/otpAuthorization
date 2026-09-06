# Authentication Microservices By OTP

A microservice-based authentication system built with **Java, Spring Boot, and Spring Security**.

The project implements authentication and authorization using jwt and is designed as a distributed system consisting of five microservices.

## Architecture

The project consists of the following microservices:

* **Access** — responsible for authentication and authorization. It issues access tokens that are used for further communication with protected services.
* **Eureka** — service discovery and registration server. It allows microservices to discover and communicate with each other.
* **Gateway** — API Gateway responsible for routing incoming requests to the appropriate microservices.
* **Config** — centralized configuration server for managing application configuration across all microservices.
* **Notification** — responsible for sending verification codes to end users during authentication and registration.

## Authentication Flow

The authentication process is handled by the **Access** service using **Spring Security**.

A simplified authentication flow:

1. The user starts the authentication or registration process.
2. The **Access** service handles the authentication request.
3. The **Notification** service sends a verification code to the user.
4. After successful verification, the **Access** service authenticates the user.
5. An access token is issued to the client.
6. The client uses the token for subsequent requests to protected services.
7. The **Gateway** routes authenticated requests to the appropriate microservices.

## Technologies

* Java
* Spring Boot
* Spring Security
* Spring Cloud Gateway
* Spring Cloud Netflix Eureka
* Spring Cloud Config
* REST API
* Microservices Architecture

## Microservices

| Service          | Responsibility                                      |
| ---------------- | --------------------------------------------------- |
| **Access**       | Authentication, authorization, and token management |
| **Eureka**       | Service discovery                                   |
| **Gateway**      | API Gateway and request routing                     |
| **Config**       | Centralized configuration management                |
| **Notification** | Sending verification codes to users                 |
