# Page Pulse - Scalable Architecture Design

## Overview

Page Pulse is a production-grade URL auditing service built using Spring Boot.

The service audits website URLs by checking:

- Website availability
- HTTP status code
- Response time
- HTML page title

The architecture is designed to support:

- 10,000 audits per day
- Bursts of up to 500 concurrent requests
- High availability
- Scalability
- Fault tolerance
- Easy deployment and monitoring

---

# High Level Architecture

```
                     +----------------------+
                     |        Client        |
                     +----------+-----------+
                                |
                                |
                                v
                     +----------------------+
                     |    Load Balancer     |
                     +----------+-----------+
                                |
                                |
                                v
                     +----------------------+
                     |      API Gateway     |
                     +----------+-----------+
                                |
               +----------------+----------------+
               |                                 |
               v                                 v
    +----------------------+          +----------------------+
    |   Rate Limiter       |          |   Request Validator  |
    +----------------------+          +----------------------+
               |
               |
               v
      +----------------------+
      |     Audit Service    |
      +----------+-----------+
                 |
        +--------+--------+
        |                 |
        |                 |
        v                 v
 +---------------+   +----------------+
 | Redis Cache   |   | Message Queue  |
 +---------------+   +--------+-------+
                              |
                              |
                     +--------+--------+
                     |                 |
                     v                 v
              +-------------+   +-------------+
              | Worker 1    |   | Worker 2    |
              +-------------+   +-------------+
                     |
                     |
                     v
             +--------------------+
             | External Websites  |
             +--------------------+
                     |
                     |
                     v
             +--------------------+
             | PostgreSQL Database|
             +--------------------+
```

---

# Request Flow

1. Client sends an audit request.

2. API Gateway receives the request.

3. Input validation verifies:
   - URL format
   - Required fields

4. Rate Limiter checks whether the client has exceeded the request limit.

5. Audit Service checks Redis Cache.

6. If cached data exists:
   - Return cached response immediately.

7. If cache is empty:
   - Create an audit job.
   - Send it to the Message Queue.

8. Worker fetches the target website.

9. Worker extracts:
   - Status code
   - Response time
   - HTML title

10. Save the result:
    - Redis Cache
    - PostgreSQL Database

11. Return the response to the client.

---

# Handling 10,000 Audits Per Day

10,000 audits per day equals approximately:

- 417 audits per hour
- 7 audits per minute

Although average traffic is low, users may generate traffic spikes.

Therefore the architecture supports:

- Horizontal scaling
- Queue-based processing
- Redis caching
- Rate limiting
- Request timeout

This allows the application to comfortably handle sudden bursts of traffic.

---

# Handling 500 Concurrent Requests

Processing every request synchronously would slow down the application.

Instead, the architecture uses asynchronous processing.

```
Client Requests

        |

        v

   Message Queue

        |

+-------+-------+-------+

|               |       |

v               v       v

Worker1      Worker2  Worker3
```

Multiple workers process jobs independently.

Benefits:

- Better throughput
- Lower latency
- No request blocking
- Improved scalability

---

# Queue Strategy

Technology Selected:

- RabbitMQ

Alternative Considered:

- Apache Kafka

Reason for choosing RabbitMQ:

- Simpler setup
- Reliable delivery
- Easy retry mechanism
- Suitable for medium-sized systems

Benefits:

- Smooth handling of traffic spikes
- Worker retries
- Fault tolerance
- Independent processing

---

# Cache Design

Technology:

Redis

Purpose:

- Store recently audited URLs
- Reduce duplicate HTTP requests
- Improve response time

Cache TTL:

```
cache.ttl = 300000
```

Default cache duration:

5 minutes

Example:

```
google.com

Status : 200

Response Time : 135 ms

Cached : true
```

Benefits:

- Faster responses
- Reduced network calls
- Lower server load

---

# Database Design

Technology:

PostgreSQL

Stores:

- Audit history
- User information
- Logs
- Reports

Sample fields:

```
Audit ID

URL

Status Code

Response Time

Page Title

Created At
```

---

# Technology Decisions

## Spring Boot

Chosen because:

- Production ready
- REST support
- Easy dependency management
- Strong Java ecosystem

Alternative rejected:

Node.js

Reason:

Spring Boot offers stronger enterprise support and easier integration for large backend systems.

---

## Redis

Chosen because:

- Extremely fast
- Distributed caching
- TTL support

Alternative rejected:

HashMap

Reason:

HashMap only works within a single application instance and cannot be shared across multiple servers.

---

## RabbitMQ

Chosen because:

- Reliable messaging
- Retry support
- Easy configuration

Alternative rejected:

Direct synchronous processing

Reason:

Synchronous requests cannot efficiently handle large traffic spikes.

---

## PostgreSQL

Chosen because:

- Reliable
- ACID compliant
- Production ready

Alternative rejected:

MySQL

Reason:

Both databases are good choices. PostgreSQL was selected because of its advanced indexing and scalability features.

---

# Failure Mode Analysis

## Failure 1

Problem:

Target website does not respond.

Solution:

- HTTP timeout
- Retry request
- Return structured error response

---

## Failure 2

Problem:

Worker crashes while processing requests.

Solution:

- RabbitMQ retains pending jobs.
- Worker restarts automatically.
- Retry failed jobs.

---

## Failure 3

Problem:

Redis cache becomes unavailable.

Solution:

- Fetch data directly from the target website.
- Store fresh response after Redis recovers.
- Continue serving users without downtime.

---

# Monitoring

Application Metrics

- Total requests
- Successful requests
- Failed requests
- Average response time
- Cache hit ratio
- Cache miss ratio

Infrastructure Metrics

- CPU usage
- Memory usage
- Queue size
- Worker availability
- Database connections

Monitoring Tools

- Prometheus
- Grafana
- ELK Stack

Alerts

- High error rate
- High response time
- Queue backlog
- Worker failure
- Server CPU above threshold
- Database connection failures

---

# Deployment Strategy

Deployment Platform

- Docker
- Render

CI/CD

GitHub Actions automatically:

- Builds the project
- Runs all JUnit tests
- Verifies successful compilation
- Deploys the latest version

---

# Rollback Plan

If a deployment fails:

1. Stop the failed deployment.
2. Deploy the previous stable Docker image.
3. Verify application health.
4. Resume normal traffic.
5. Investigate logs before redeployment.

This minimizes downtime and ensures service reliability.

---

# Scalability Improvements

Future improvements include:

- Kubernetes deployment
- Auto Scaling
- Redis Cluster
- Multiple Audit Workers
- CDN support
- Distributed Logging
- Health Checks
- Circuit Breaker
- Metrics Dashboard

---

# Conclusion

The proposed architecture transforms Page Pulse into a scalable production-ready system capable of handling more than 10,000 audits per day with bursts of 500 concurrent requests.

The use of Redis caching, RabbitMQ, PostgreSQL, Spring Boot, Docker, and GitHub Actions provides a reliable, maintainable, and fault-tolerant architecture suitable for real-world deployment.