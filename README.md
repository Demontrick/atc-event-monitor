# atc-event-monitor

A real-time flight event monitoring system built to demonstrate 
backend engineering capability for Thales's Air Mobility 
Solutions (AMS) team in Portugal.

Thales ATC systems process safety-critical flight events across 
international airspace — collision risks, airspace breaches, 
runway conflicts, emergencies — where every event must be 
captured, streamed, persisted, and actioned with zero tolerance 
for loss or delay. This system replicates that pipeline at a 
demonstrable scale.

---

## 🧠 Why This Project Exists

Modern air traffic control systems process thousands of 
real-time signals per second across distributed infrastructure. 
This PoC simulates a lightweight event-driven monitoring system 
demonstrating:

- Real-time event ingestion via Kafka
- Stream processing and persistent storage via MongoDB
- Backend aggregation exposed through a clean REST API
- Live operator dashboard with ACK / RESOLVE workflow
- Distributed system thinking applied to a safety-critical domain

This mirrors how real ATC and defense monitoring systems are 
architected at a production-representative level.

---

## 🏗 System Architecture

Kafka Producer
↓
Kafka Topic (flight-events)
↓
Spring Boot Consumer
↓
MongoDB (Event Store)
↓
REST API Layer
↓
Angular Frontend (Live Polling Dashboard)

---

## ⚙️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3, Java 21 |
| Streaming | Apache Kafka |
| Database | MongoDB |
| Frontend | Angular 19, TypeScript |
| Infrastructure | Docker Compose, Zookeeper |

---

## 📡 Flight Event Types

| Event | ATC Context |
|---|---|
| EMERGENCY_DECLARED | Aircraft declared emergency — immediate escalation |
| COLLISION_RISK | Proximity alert between two aircraft |
| AIRSPACE_BREACH | Aircraft entered restricted airspace |
| FLIGHT_DELAYED | Departure or arrival delay logged |
| RUNWAY_CONFLICT | Simultaneous runway access detected |

---

## ⚖️ Severity Levels

| Level | Operator Response |
|---|---|
| LOW | Informational — monitor only |
| MEDIUM | Review within current shift |
| HIGH | Immediate operator attention required |
| CRITICAL | Escalate to supervisor now |

---

## 🔌 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | /api/flights/events | Paginated event feed |
| GET | /api/flights/events?severity=CRITICAL | Filtered events |
| PATCH | /api/flights/events/{id}/status | Update event status |
| POST | /api/flights/events/publish | Publish test event |
| GET | /api/flights/events/stats | Counts by severity and type |

```bash
# Get all CRITICAL events
curl http://localhost:8080/api/flights/events?severity=CRITICAL

# Acknowledge an event
curl -X PATCH \
  -H "Content-Type: application/json" \
  -d '{"status": "ACKNOWLEDGED"}' \
  http://localhost:8080/api/flights/events/abc123/status
```

---

## 🚀 Core Features

### ✈ Real-Time Event Stream
- Simulated flight events across real flight identifiers 
  (EK505, BA202, etc.)
- Kafka producer continuously publishes events covering 
  all event types and severity levels
- Consumer persists every event to MongoDB with no loss

### 📊 Live Operator Dashboard
- Auto-refreshing event feed via RxJS polling
- Severity filter: ALL / LOW / MEDIUM / HIGH / CRITICAL
- Event type filter: ALL / COLLISION_RISK / 
  AIRSPACE_BREACH / etc.
- Event cards: flightId, aircraftType, severity badge, 
  type badge, timestamp, current status
- ACK and RESOLVE actions per event

### 📡 Live Stats Engine
- Aggregated severity counts derived from event stream
- Frontend-side consistency model — stats stay in sync 
  with event feed without additional API calls

---

## 💡 Key Design Decisions

### 1. Event-Driven Architecture
Kafka simulates real-world aviation telemetry streams — 
producer and consumer are decoupled, allowing either side 
to scale independently.

### 2. Stateless Frontend
Angular derives all state from backend polling — no 
permanent client-side state that can drift from the 
source of truth.

### 3. Derived Stats Model
Stats are computed from the live event stream rather than 
a separate aggregation endpoint — ensures consistency 
between feed and counters at all times.

### 4. Deduplication Layer
Frontend includes deduplication logic to handle repeated 
Kafka emissions safely — critical in high-frequency 
event environments.

---

## ⚠️ Known Limitations (PoC Scope)

- No authentication layer (planned: JWT + role-based access)
- Polling used instead of WebSockets (planned: STOMP)
- No persistent event replay system
- Kafka simplified for simulation — single broker, 
  no replication

---

## 🐳 Run with Docker

```bash
docker compose up
```

Spins up:
- Zookeeper
- Kafka (port 9092)
- MongoDB (port 27017)
- Spring Boot Backend (port 8080)
- Angular Frontend (port 4200)

### Run individually:

```bash
# Backend
mvn spring-boot:run

# Frontend
npm install
ng serve
```

---

## 🔥 What This Project Demonstrates

- Event-driven system design in a safety-critical domain
- Real-time Kafka-based data pipelines
- Frontend-backend synchronization at scale
- Distributed system thinking applied to ATC architecture
- Production-style Angular 19 standalone components
- Clean REST API design with MongoDB persistence

---

## 🚀 Future Improvements

- Replace polling with WebSockets (STOMP)
- Add JWT authentication with ATC_OPERATOR / 
  SUPERVISOR roles
- Add event replay and history view
- Add Grafana monitoring for Kafka streams
- Deploy via Kubernetes
