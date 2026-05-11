# ATC Event Monitor

Real-time flight event monitoring system built on Kafka, Spring Boot, and Angular — modelled on the event pipeline architecture used in Air Traffic Control and distributed safety-critical monitoring systems.

---

## System overview

Air traffic control environments process continuous streams of flight events across distributed infrastructure — collision risks, airspace breaches, runway conflicts, and declared emergencies — where reliable ingestion, persistence, and operator visibility are non-negotiable.

This project implements an event-driven monitoring pipeline applying those architectural patterns: Kafka-based event ingestion, Spring Boot stream processing, MongoDB persistence, a secured REST API layer, and a live Angular operator dashboard with ACK/RESOLVE workflow.

---

## Architecture

```
Kafka Producer
    ↓
Kafka Topic (flight-events)
    ↓
Spring Boot Consumer (flight-events-consumer-group)
    ↓
MongoDB (Event Store)
    ↓
REST API Layer (Spring Boot)
    ↓
Angular Dashboard (Live Operator UI)
```

---

## Tech stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security, Spring Data MongoDB |
| Messaging | Apache Kafka, Zookeeper |
| Database | MongoDB |
| Frontend | Angular 19, TypeScript |
| Infrastructure | Docker Compose, Kubernetes (Deployment, Service, ConfigMap) |
| Observability | Spring Boot Actuator (`/health`, `/info`), SLF4J + MDC correlation IDs, JSON structured logs |

---

## Security

Role-based access control implemented with Spring Security. Endpoint-level authorization enforced at the controller layer.

| Role | Permissions |
|---|---|
| `OPERATOR` | Read events, update event status |
| `SUPERVISOR` | Full access including event publishing |

In-memory user store for local development. Designed for LDAP or OAuth2 provider integration in a production deployment.

**Example credentials**

```
operator / operator123
supervisor / supervisor123
```

---

## REST API

RESTful API built with Spring Boot, secured with Spring Security, and consumed by the Angular operator dashboard.

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/api/flights/events` | OPERATOR | Paginated event feed |
| `GET` | `/api/flights/events?severity=CRITICAL` | OPERATOR | Filtered by severity or type |
| `PATCH` | `/api/flights/events/{id}/status` | OPERATOR | Update event status |
| `POST` | `/api/flights/events/publish` | SUPERVISOR | Publish a test event |
| `GET` | `/api/flights/events/stats` | OPERATOR | Counts by severity and type |
| `GET` | `/actuator/health` | Secured | Service and Kafka consumer health |

**Example requests**

```bash
# Retrieve all CRITICAL events
curl -u operator:operator123 \
  http://localhost:8080/api/flights/events?severity=CRITICAL

# Acknowledge an event
curl -u operator:operator123 \
  -X PATCH \
  -H "Content-Type: application/json" \
  -d '{"status": "ACKNOWLEDGED"}' \
  http://localhost:8080/api/flights/events/abc123/status
```

---

## Flight event types

| Event | Severity range | ATC context |
|---|---|---|
| `EMERGENCY_DECLARED` | CRITICAL | Aircraft declared emergency — immediate escalation |
| `COLLISION_RISK` | HIGH / CRITICAL | Proximity alert between two aircraft |
| `AIRSPACE_BREACH` | MEDIUM / HIGH | Aircraft entered restricted airspace |
| `RUNWAY_CONFLICT` | HIGH / CRITICAL | Simultaneous runway access detected |
| `FLIGHT_DELAYED` | LOW / MEDIUM | Departure or arrival delay logged |

---

## Key implementation details

**Event-driven pipeline**
Kafka producer publishes typed flight events to the `flight-events` topic. Spring Boot consumer group (`flight-events-consumer-group`) processes and persists events to MongoDB. A deduplication layer handles repeated Kafka emissions (at-least-once delivery) safely.

**Spring Security RBAC**
Endpoint-level authorization enforced at the controller layer. `OPERATOR` role scoped to read and status-update operations. `SUPERVISOR` role required for event publishing. Actuator endpoints secured independently.

**Observability**
Spring Boot Actuator exposes `/actuator/health` (including Kafka consumer connectivity) and `/actuator/info`. Per-request correlation IDs (MDC) propagated across the Kafka consumer and REST layers for end-to-end log tracing. Logs emitted in JSON format for compatibility with standard log aggregation platforms.

**Angular operator dashboard**
Angular 19 standalone components with TypeScript. Live event feed with severity and type filtering. ACK / RESOLVE workflow per event. Stateless frontend derives state from polling to prevent client-side drift.

**Kubernetes manifests**
`Deployment` and `Service` resources for the Spring Boot backend. `ConfigMap` for Kafka broker and MongoDB configuration. Liveness probe configured against `/actuator/health`.

---

## Running locally

```bash
docker compose up --build
```

Starts: Zookeeper, Kafka (port 9092), MongoDB (port 27017), Spring Boot backend (port 8080), Angular frontend (port 4200).

Environment configuration managed via Spring profiles:
- `application-dev.properties` — local Kafka and MongoDB endpoints
- `application-prod.properties` — environment variable overrides for broker URLs and database connections

---

## Project structure

```
atc-event-monitor/
├── backend/                   # Spring Boot 3 application
│   ├── consumer/              # Kafka consumer + deduplication
│   ├── controller/            # REST API controllers
│   ├── security/              # Spring Security configuration
│   ├── model/                 # FlightEvent domain model
│   └── repository/            # Spring Data MongoDB repositories
├── frontend/                  # Angular 19 operator dashboard
├── k8s/                       # Kubernetes manifests
│   ├── deployment.yaml
│   ├── service.yaml
│   └── configmap.yaml
└── docker-compose.yml
```