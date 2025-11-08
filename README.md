# 🏦 e-Wallet Microservices Design Specification
**Version:** 1.4  
**Date:** 8-Nov-2025  
**Author:** Rajasekhar Setty  

---

## 🧩 Overview
The **e-Wallet Management System** is designed as a distributed microservices architecture supporting wallet-based purchases, merchant settlements, and notifications.

**Technology Stack:**
- Spring Boot 3.3.4
- Java 20
- Spring Data JPA / Hibernate 6
- PostgreSQL
- REST APIs (HTTP/JSON)
- Maven Build Tool
- Swagger / OpenAPI Documentation

Each service maintains its own database schema and communicates over REST endpoints.

---

## 🧱 Standard Audit Model
All entities extend a `BaseAudit` superclass containing audit metadata and optimistic locking.

| Field | Type | Description |
|--------|------|-------------|
| `created_at` | TIMESTAMPTZ | Creation timestamp |
| `created_by` | VARCHAR(100) | Created by user/system |
| `updated_at` | TIMESTAMPTZ | Last modification timestamp |
| `updated_by` | VARCHAR(100) | Updated by user/system |
| `version` | BIGINT | Version number for optimistic locking |

---

## 🏗️ Architecture Summary

| Service | Responsibility | Key Models |
|----------|----------------|-------------|
| **Customer Service** | Manages customers, wallets, merchants, and purchase orders | Customer, Wallet (1:1), Order, Product, Merchant |
| **Wallet Service** | Handles balance, debits, credits, and transaction history | Wallet, WalletTransaction |
| **Order Service** | Manages order creation, validation, and tracking | Order, Product |
| **Inventory Service** | Maintains product catalog and stock validation | Product (`total_quantity`) |
| **Notification Service** | Sends merchant notifications | Notification |
| **Payment Service** | Handles merchant settlement and payment tracking | Merchant, Payment |

---

## ⚙️ Common Technologies

- **Language:** Java 20  
- **Framework:** Spring Boot 3.3.4  
- **Persistence:** Spring Data JPA (Hibernate 6)  
- **Database:** PostgreSQL  
- **API Documentation:** Springdoc OpenAPI / Swagger UI  
- **Communication:** REST (JSON over HTTP)

---

## 📁 Microservice Specifications

### 1️⃣ Customer Service

**Purpose:**  
Stores and retrieves customers, merchants, wallets, and purchase orders.

**Endpoints:**

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/customers/` | Create Customer and Wallet Detais |

	API Request format:

	{
	  "name": "Ananya Rao",
	  "email": "ananya.rao@example.com",
	  "wallet": {
		"balance": 5000.00,
		"currency": "INR"
	  }
	}


| `GET` | `/customers/{id}` | Get customer details including wallet |

		API Response:
		{
		  "id": 101,
		  "name": "Ananya Rao",
		  "email": "ananya.rao@example.com",
		  "wallet": {
			"id": 201,
			"customerId": 101,
			"balance": 5000.00,
			"currency": "INR"
		  }
		}
| `POST` | `/purchaseOrders` | Create a new order (list of products with `order_quantity`) |
		POST API Request

		{
		  "name": "Ananya Rao",
		  "email": "ananya.rao@example.com",
		  "orders": [
			{
			  "status": "CREATED",
			  "currency": "INR",
			  "products": [
				{
				  "name": "Wireless Mouse",
				  "description": "2.4 GHz ergonomic mouse with USB receiver",
				  "price": 799.00,
				  "currency": "INR",
				  "merchantId": 501,
				  "order_quantity": 1
				},
				{
				  "name": "Mechanical Keyboard",
				  "description": "RGB backlit keyboard with blue switches",
				  "price": 3499.00,
				  "currency": "INR",
				  "merchantId": 501,
				  "order_quantity": 1
				}
			  ]
			}
		  ]
		}


**Entities:**

```java
class Customer extends BaseAudit {
    Long id;
    String name;
    String email;
    Wallet wallet;
}

class Wallet extends BaseAudit {
    Long id;
    Long customerId;
    Double balance;
    String currency;
}

class Merchant extends BaseAudit {
    Long id;
    String name;
    String email;
    String currency;
}

class Product extends BaseAudit {
    Long id;
    Long orderId;
    String name;
    String description;
    Double price;
    String currency;
    Long merchantId;
    Integer order_quantity;
}

class Order extends BaseAudit {
    Long id;
    Long customerId;
    List<Product> products;
    String status;
    Double totalAmount;
    String currency;
}
```

---

### 2️⃣ Wallet Service

**Purpose:**  
Handles wallet operations, including balance updates and transaction history.

**Endpoints:**

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/wallets/{walletId}/balance` | Retrieve wallet balance |
| `POST` | `/wallets/{walletId}/debit` | Debit wallet balance |
| `POST` | `/wallets/{walletId}/credit` | Credit wallet balance |

**Entities:**

```java
class Wallet extends BaseAudit {
    Long id;
    Long customerId;
    Double balance;
    String currency;
}

class WalletTransaction extends BaseAudit {
    Long id;
    Long walletId;
    Double amount;
    String type;
    String referenceId;
}
```

---

### 3️⃣ Order Service

**Purpose:**  
Handles order lifecycle, including validation with Inventory and Wallet services.

**Endpoints:**

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/orders` | Create an order |
| `GET` | `/orders/{orderId}/status` | Check order status |

**Entities:**

```java
class Order extends BaseAudit {
    Long id;
    Long customerId;
    List<Product> products;
    String status;
    Double totalAmount;
    String currency;
}

class Product extends BaseAudit {
    Long id;
    Long orderId;
    String name;
    Double price;
    String currency;
    Long merchantId;
    Integer order_quantity;
}
```

---

### 4️⃣ Inventory Service

**Purpose:**  
Manages products and stock levels.

**Endpoints:**

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/products` | List all products |
| `POST` | `/products/update` | Validate and Update stock after order |

**Entity:**

```java
class Product extends BaseAudit {
    Long id;
    String name;
    String description;
    Double price;
    String currency;
    Long merchantId;
    Integer total_quantity;
}
```

---

### 5️⃣ Notification Service

**Purpose:**  
Sends notifications to merchants after successful payments.

**Endpoints:**

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/notifications` | Store new notification |
| `POST` | `/notifications/send` | Send notification email |

**Entity:**

```java
class Notification extends BaseAudit {
    Long id;
    Long merchantId;
    String merchantName;
    Double amount;
    String status;
    String emailId;
}
```

---

### 6️⃣ Payment Service

**Purpose:**  
Handles merchant settlements and transaction history.

**Endpoints:**

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/payments/settle` | Settle merchant payment |
| `GET` | `/payments/{merchantId}` | Retrieve merchant payment history |

**Entities:**

```java
class Merchant extends BaseAudit {
    Long id;
    String name;
    String email;
    Double totalReceived;
    String currency;
}

class Payment extends BaseAudit {
    Long id;
    Long merchantId;
    Double amount;
    String status;
    LocalDateTime settledAt;
}

Distributed Transactions

1) Choose the Saga style

Choreography (recommended here): each service publishes domain events and reacts to others. Simple, fast to evolve.

Orchestration (alternative): one “order-saga” coordinator commands each step. Useful if flows get very complex.

Given your “Order ↔ Inventory ↔ Wallet ↔ Payment ↔ Notification” flow, start with choreography; introduce an orchestrator later if business logic explodes.

2) The business flow (happy path)

Create Order → Reserve Stock → Authorize Wallet → Settle Payment → Send Notification → Mark Order Completed

Order Service

API: POST /orders creates order in status CREATED.

Emits OrderCreated event.

Inventory Service

On OrderCreated: validate and reserve quantities (total_quantity stays; keep reserved_quantity field or reservation rows).

If success → emit InventoryReserved.

If failure → emit InventoryRejected.

Wallet Service

On InventoryReserved: authorize (hold) the amount (don’t capture yet).

If success → emit WalletAuthorized.

If failure → emit WalletAuthorizationFailed.

Payment Service

On WalletAuthorized: settle to merchant (or mark SETTLING and complete asynchronously).

Emit PaymentSettled or PaymentFailed.

Notification Service

On PaymentSettled: send merchant email/SMS; emit NotificationSent (optional).

Order Service

On PaymentSettled: set order COMPLETED.

On any failure event: set order CANCELLED and trigger compensations (below).

3) Compensations (failure paths)

If InventoryRejected → Order CANCELLED.

If WalletAuthorizationFailed → Inventory releases reservation (InventoryReleaseRequested).

If PaymentFailed after WalletAuthorized:

Payment emits failure → Wallet voids the authorization (no money captured).

Inventory releases the reservation.

Order CANCELLED.

If you ever captured money and then fail later, emit RefundRequested and let Payment execute a refund; Wallet credits back.
---

## 🗄️ Database Ownership Summary

| Service | Tables |
|----------|--------|
| Customer | customer, wallet, merchant, order, order_product |
| Wallet | wallet, wallet_transaction |
| Order | order, order_product |
| Inventory | product |
| Notification | notification |
| Payment | merchant, payment |

---


## 🗄️ PostgreSQL Schema (per service, with audit fields)

> All tables include: `created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`, `created_by VARCHAR(100) NOT NULL`, `updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`, `updated_by VARCHAR(100) NOT NULL`, `version BIGINT NOT NULL DEFAULT 0`.

### Customer Service

```sql
CREATE TABLE customer (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  email VARCHAR(200) UNIQUE NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE wallet (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT UNIQUE NOT NULL REFERENCES customer(id),
  balance NUMERIC(18,2) NOT NULL DEFAULT 0,
  currency VARCHAR(10) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE merchant (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  email VARCHAR(200) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE "order" (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT NOT NULL REFERENCES customer(id),
  status VARCHAR(30) NOT NULL,
  total_amount NUMERIC(18,2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE order_product (
  id BIGSERIAL PRIMARY KEY,
  order_id BIGINT NOT NULL REFERENCES "order"(id) ON DELETE CASCADE,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  price NUMERIC(18,2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  merchant_id BIGINT NOT NULL REFERENCES merchant(id),
  order_quantity INT NOT NULL CHECK (order_quantity > 0),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);
```

### Wallet Service

```sql
CREATE TABLE wallet (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT UNIQUE NOT NULL,
  balance NUMERIC(18,2) NOT NULL DEFAULT 0,
  currency VARCHAR(10) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE wallet_transaction (
  id BIGSERIAL PRIMARY KEY,
  wallet_id BIGINT NOT NULL REFERENCES wallet(id) ON DELETE CASCADE,
  amount NUMERIC(18,2) NOT NULL,
  type VARCHAR(10) NOT NULL CHECK (type IN ('DEBIT','CREDIT')),
  reference_id VARCHAR(100),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);
CREATE INDEX idx_wallet_tx_wallet_id ON wallet_transaction(wallet_id);
```

### Order Service

```sql
CREATE TABLE "order" (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  status VARCHAR(30) NOT NULL,
  total_amount NUMERIC(18,2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE order_product (
  id BIGSERIAL PRIMARY KEY,
  order_id BIGINT NOT NULL REFERENCES "order"(id) ON DELETE CASCADE,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  price NUMERIC(18,2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  merchant_id BIGINT NOT NULL,
  order_quantity INT NOT NULL CHECK (order_quantity > 0),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);
CREATE INDEX idx_order_product_order_id ON order_product(order_id);
```

### Inventory Service

```sql
CREATE TABLE product (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  price NUMERIC(18,2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  merchant_id BIGINT NOT NULL,
  total_quantity INT NOT NULL CHECK (total_quantity >= 0),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);
```

### Notification Service

```sql
CREATE TABLE notification (
  id BIGSERIAL PRIMARY KEY,
  merchant_id BIGINT NOT NULL,
  merchant_name VARCHAR(200) NOT NULL,
  amount NUMERIC(18,2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  email_id VARCHAR(200) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);
```

### Payment Service

```sql
CREATE TABLE merchant (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  email VARCHAR(200) NOT NULL,
  total_received NUMERIC(18,2) NOT NULL DEFAULT 0,
  currency VARCHAR(10) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE payment (
  id BIGSERIAL PRIMARY KEY,
  merchant_id BIGINT NOT NULL REFERENCES merchant(id) ON DELETE CASCADE,
  amount NUMERIC(18,2) NOT NULL,
  status VARCHAR(20) NOT NULL CHECK (status IN ('INITIATED','SETTLED','FAILED')),
  settled_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(100) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by VARCHAR(100) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);
CREATE INDEX idx_payment_merchant_id ON payment(merchant_id);



## 🚀 Development Tasks Summary

| Service | Tasks |
|----------|-------|
| Customer | CRUD APIs, order creation, wallet validation |
| Wallet | Balance mgmt, debit/credit, transaction logging |
| Order | Validate stock & balance, update status |
| Inventory | Validate & update stock quantities |
| Notification | Merchant email notifications |
| Payment | Settlement & payout tracking |

---

## 🧠 Future Enhancements

- Add authentication & authorization
- Introduce Kafka-based async communication
- Implement service discovery & centralized configuration
- Enable OpenTelemetry distributed tracing
- Add analytics & reporting dashboards

---
