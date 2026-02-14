# 🛒 E-Commerce Microservices System

A production-grade **Event-Driven E-Commerce Backend System** built using Spring Boot Microservices architecture with Kafka, Outbox Pattern, Inbox Pattern, Saga Pattern, and Idempotency.

---

# 📌 Project Overview

This project simulates a real-world distributed e-commerce backend system where:

- Order creation triggers distributed processing
- Payment and Inventory services work independently
- Events are communicated using Apache Kafka
- Data consistency is maintained using Saga Pattern
- Reliability is ensured using Outbox & Inbox patterns
- Duplicate event processing is prevented using Idempotency

This architecture reflects how large-scale distributed systems operate in production.

---

# 🏗️ Architecture

## Services

1. **Order Service** (Saga Orchestrator)
2. **Payment Service**
3. **Inventory Service**
4. **Cart Service (Redis)**
5. **Apache Kafka**
6. **MySQL (Database per service)**

---

# 🔁 Order Processing Flow

1. User creates order.
2. Order Service:
   - Saves order in DB.
   - Inserts `OrderCreatedEvent` into Outbox table.
3. Outbox Publisher sends event to Kafka.
4. Payment Service consumes event → processes payment.
5. Inventory Service consumes event → reserves stock.
6. Both services publish result events.
7. Order Service consumes:
   - `PaymentCompletedEvent`
   - `InventoryReservedEvent`
8. If both succeed → Order CONFIRMED.
9. If any fails → Order CANCELLED.

---

# 🧠 Design Patterns Implemented

## 1️⃣ Saga Pattern (Choreography)

There is no distributed transaction.  
Each service performs a local transaction and emits an event.

Services react to events independently.

---

## 2️⃣ Outbox Pattern

Ensures reliable event publishing.

### Outbox Table Structure

```sql
CREATE TABLE outbox_events (
    event_id VARCHAR(255) PRIMARY KEY,
    aggregated_id BIGINT,
    event_type VARCHAR(255),
    pay_load TEXT,
    status VARCHAR(50),
    topic VARCHAR(255),
    attempts INT,
    created_at TIMESTAMP,
    sent_at TIMESTAMP
);
```

Events are:
- Stored in DB
- Published asynchronously
- Marked as SENT after successful Kafka publish

---

## 3️⃣ Inbox Pattern

Prevents duplicate event processing.

### Inbox Table Structure

```sql
CREATE TABLE inbox_events (
    event_id VARCHAR(255) PRIMARY KEY,
    event_type VARCHAR(255),
    received_at TIMESTAMP
);
```

Before processing an event:

```java
boolean isNew = inboxService.markReceivedIfNew(eventId, eventType);
if (!isNew) return;
```

---

## 4️⃣ Idempotency

Implemented at multiple levels:

| Layer | Protection |
|-------|------------|
| Producer | Kafka idempotent producer |
| Consumer | Inbox table |
| Payment Service | Prevent duplicate payment |
| Database | Unique constraints |

---

# 📦 Event Structure

All services use a common event wrapper:

```java
public class EventEnvelope<T> {
    private String eventId;
    private String eventType;
    private Long aggregatedId;
    private Instant occurTime;
    private Integer version;
    private T payload;
}
```

This ensures:
- Standardized event format
- Version control
- Traceability
- Extensibility

---

# ⚙️ Kafka Configuration

## Producer Configuration

```yaml
producer:
  key-serializer: org.apache.kafka.common.serialization.StringSerializer
  value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
  retries: 2147483647
  properties:
    acks: all
    enable-idempotence: true
    max-in-flight-requests-per-connection: 5
```

### Explanation

- `acks=all` → Strong durability
- `enable-idempotence=true` → Prevent duplicate messages
- Infinite retries → Reliable publish

---

## Consumer Configuration

```yaml
consumer:
  group-id: order-service-group
  auto-offset-reset: earliest
  enable-auto-commit: false
  key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
  value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```

Manual offset control ensures safe processing.

---

# 🧱 Database Per Service

Each service has its own database:

- order_db
- payment_db
- inventory_db

This ensures:
- Loose coupling
- Independent scaling
- Service isolation

---

# 🗂️ Project Structure

```
ecommerce-system/
│
├── order-service/
│   ├── controller/
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── kafka/
│   ├── config/
│   └── events/
│
├── payment-service/
│   ├── entity/
│   ├── service/
│   ├── kafka/
│   └── events/
│
├── inventory-service/
│   ├── entity/
│   ├── service/
│   ├── kafka/
│   └── events/
│
├── cart-service/
│   ├── model/
│   ├── service/
│   └── config/
```

---

# 🚀 How to Run

## 1️⃣ Start Infrastructure

- Start Zookeeper
- Start Kafka
- Start MySQL
- Start Redis

## 2️⃣ Start Services (Order)

1. Inventory Service
2. Payment Service
3. Order Service
4. Cart Service

---

# 🧪 How to Test

1. Add items to cart
2. Create order
3. Observe:
   - Outbox entries created
   - Kafka messages published
   - Payment processed
   - Inventory reserved
   - Order confirmed

---

# ⚠️ Failure Handling

| Scenario | Behavior |
|----------|----------|
| Payment fails | Order cancelled |
| Inventory fails | Order cancelled |
| Duplicate event | Ignored via Inbox |
| Kafka retry | Safe via idempotency |
| Service crash | Outbox ensures retry |

---

# 🔍 Observability

Logs include:

- Event received
- Event ignored (duplicate)
- Order status update
- Payment result
- Inventory reservation

---

# 🛠️ Tech Stack

- Java 17+
- Spring Boot 3
- Spring Data JPA
- Spring Kafka
- MySQL
- Redis
- Lombok
- Apache Kafka

---

# 🎯 What This Project Demonstrates

- Event-driven microservices
- Distributed system design
- Eventually consistent architecture
- Reliable messaging
- Idempotent consumers
- Fault-tolerant services
- Production-ready backend engineering

---

# 📌 Future Improvements

- Circuit Breaker (Resilience4j)
- Distributed Tracing (Zipkin)
- Metrics (Prometheus + Grafana)
- Docker & Kubernetes deployment
- API Gateway
- JWT Authentication

---

# 🏁 Conclusion

This project implements enterprise-grade distributed system patterns including:

- Saga Pattern
- Outbox Pattern
- Inbox Pattern
- Idempotent Producers
- Idempotent Consumers

It reflects real-world backend engineering principles used in high-scale production systems.

---

**Author:** Rajnikant Kumar  
**Architecture Style:** Event-Driven Microservices  
**Consistency Model:** Eventually Consistent  
**Communication:** Asynchronous via Kafka  

