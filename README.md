# Page Pulse

Page Pulse is a Spring Boot web application that audits websites by checking their availability, response time, HTTP status, and page title. It also uses an in-memory cache to improve performance.

## Features

- Audit any website URL
- Fetch HTTP status code
- Measure response time
- Extract page title
- In-memory caching
- REST API
- Unit Tests with JUnit
- GitHub Actions CI

## Tech Stack

- Java 17
- Spring Boot
- Maven
- JUnit 5
- GitHub Actions

## Run Locally

```bash
git clone https://github.com/Priyanshu-Tiwari-no1/page-pulse.git
cd page-pulse
./mvnw spring-boot:run
```

## Run Tests

```bash
./mvnw test
```

## API

Example:

```
GET /audit?url=https://google.com
```

Example Response

```json
{
  "url": "https://google.com",
  "statusCode": 200,
  "responseTime": 120,
  "title": "Google",
  "cached": false
}
```

## CI

GitHub Actions automatically builds and tests the project on every push.

## Author

Priyanshu Tiwari