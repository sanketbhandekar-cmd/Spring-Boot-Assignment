# Leave Management API

## Project Overview
This project is a Leave Management REST API built using Spring Boot for an HR system. Employees can submit leave requests, view existing requests, update them, and delete them.

Key details:
- Follows layered architecture: Controller -> Service -> Repository.
- Uses an in-memory repository with ConcurrentHashMap for thread-safe data storage without an external database.
- Uses constructor-based dependency injection.
- Uses DTOs for request and response models with Bean Validation.
- Global exception handling using @RestControllerAdvice.
- Configured with 'dev' Spring profile.
- Contains unit tests for the service layer and MockMvc tests for the controller layer.

## Build and Run Instructions

### Prerequisites
- Java 21
- Gradle (gradle wrapper is included in the project)

### Build
To build the project:
```bash
./gradlew build
```
On Windows:
```cmd
.\gradlew.bat build
```

### Run
To run the Spring Boot application:
```bash
./gradlew bootRun
```
On Windows:
```cmd
.\gradlew.bat bootRun
```

The server will start on port 8080: `http://localhost:8080`

The active profile is set to `dev` in `application.properties`.

## API Endpoint Summary

Base path: `/leaves`

1. GET /leaves
- Description: Retrieve all leave requests
- Response: 200 OK with a list of leave requests

2. GET /leaves/{id}
- Description: Retrieve a single leave request by ID
- Response: 200 OK with leave request details (or 404 Not Found if ID does not exist)

3. POST /leaves
- Description: Create a new leave request
- Request Body:
```json
{
  "employeeId": "EMP101",
  "leaveType": "CASUAL",
  "startDate": "2026-12-01",
  "endDate": "2026-12-05",
  "reason": "Personal work"
}
```
- Response: 201 Created with the saved leave request (status defaults to "PENDING")

4. PUT /leaves/{id}
- Description: Update an existing leave request
- Request Body:
```json
{
  "leaveType": "SICK",
  "startDate": "2026-12-01",
  "endDate": "2026-12-04",
  "reason": "Doctor appointment",
  "status": "APPROVED"
}
```
- Response: 200 OK with the updated leave request (or 404 Not Found if ID does not exist)

5. DELETE /leaves/{id}
- Description: Delete a leave request by ID
- Response: 204 No Content (or 404 Not Found if ID does not exist)

## Testing Instructions

To run the test suite:
```bash
./gradlew test
```
On Windows:
```cmd
.\gradlew.bat test
```

Included tests:
- LeaveServiceTest: Unit tests for the service layer using JUnit 5 and Mockito, testing positive and negative cases.
- LeaveControllerTest: Tests for REST controller endpoints using MockMvc, testing status codes, validation errors, and responses.
- LeaveManagementApiApplicationTests: Verifies application context loads successfully.

Test reports are generated at `build/reports/tests/test/index.html`.
