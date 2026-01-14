# OAuth2 Gateway Server & BFF with Next.js

A microservices architecture implementation demonstrating **API Gateway**, **Backend for Frontend (BFF)** pattern, **Service Discovery**, and **Circuit Breaker** patterns using Spring Cloud and Spring Boot.

## 🏗️ Architecture Overview

This project implements a microservices-based e-commerce platform with the following components:

```
┌─────────────────────────────────────────────────────────────────┐
│                         Client Layer                            │
│                    (Next.js Frontend - WIP)                     │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           │ HTTP/REST
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                    API Gateway (Port 8888)                      │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  • Route Management                                       │  │
│  │  • Circuit Breaker (Resilience4j)                        │  │
│  │  • CORS Configuration                                     │  │
│  │  • BFF Aggregation Endpoint                              │  │
│  │  • Load Balancing via Eureka                             │  │
│  └──────────────────────────────────────────────────────────┘  │
└──────────┬───────────────────────┬──────────────────────────────┘
           │                       │
           │                       │
           ▼                       ▼
┌──────────────────────┐  ┌──────────────────────┐
│  Product Service     │  │  Category Service    │
│    (Port 8081)       │  │    (Port 8082)       │
│                      │  │                      │
│  • CRUD Operations   │  │  • CRUD Operations   │
│  • JPA/Hibernate     │  │  • JPA/Hibernate     │
│  • PostgreSQL        │  │  • PostgreSQL        │
└──────────┬───────────┘  └──────────┬───────────┘
           │                         │
           │                         │
           ▼                         ▼
┌─────────────────────────────────────────────┐
│      PostgreSQL Database (Port 5555)        │
│  • product_db                               │
│  • category_db                              │
└─────────────────────────────────────────────┘
           ▲                         ▲
           │                         │
           └─────────┬───────────────┘
                     │
           ┌─────────▼──────────┐
           │  Eureka Server     │
           │   (Port 8761)      │
           │                    │
           │ Service Discovery  │
           └────────────────────┘
```

## 🎯 Key Features

### 1. **API Gateway Pattern**
- Centralized entry point for all client requests
- Request routing to appropriate microservices
- Cross-cutting concerns (CORS, logging, security)
- Circuit breaker integration for fault tolerance

### 2. **Backend for Frontend (BFF)**
- `/bff/dashboard` endpoint aggregates data from multiple services
- Reduces client-side API calls (1 call instead of 2)
- Parallel non-blocking service calls using WebClient
- Frontend-optimized response structure

### 3. **Service Discovery**
- Netflix Eureka for dynamic service registration
- Enables load balancing and service discovery
- Supports horizontal scaling

### 4. **Circuit Breaker Pattern**
- Resilience4j implementation
- Protects against cascading failures
- Graceful degradation with fallback endpoints
- Configurable thresholds and retry policies

### 5. **Microservices**
- **Product Service**: Manages product catalog
- **Category Service**: Manages product categories
- Independent deployability
- Database per service pattern

## 🛠️ Technologies Used

### Backend
- **Spring Boot 4.0.1** - Application framework
- **Spring Cloud Gateway** - API Gateway
- **Spring Cloud Netflix Eureka** - Service discovery
- **Spring Data JPA** - Data persistence
- **Resilience4j** - Circuit breaker
- **Project Lombok** - Boilerplate reduction
- **Java 21** - Programming language

### Database
- **PostgreSQL 17.5** - Relational database

### Build Tools
- **Gradle** - Dependency management and build automation

### Frontend (Planned)
- **Next.js** - React framework (routing configured, implementation pending)

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
- **Docker & Docker Compose** (for PostgreSQL)
- **Gradle** (or use the included Gradle wrapper)
- **Node.js & npm** (for Next.js frontend when implemented)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/RenMakara/Oauth2-inside-GatewayServer-and-BFF-with-Nextjs.git
cd Oauth2-inside-GatewayServer-and-BFF-with-Nextjs
```

### 2. Start PostgreSQL Database

Navigate to the postgres-db directory and start the database:

```bash
cd postgres-db
docker-compose up -d
```

This will:
- Start PostgreSQL on port 5555
- Create two databases: `product_db` and `category_db`
- Use credentials: `itpusr` / `itp@168`

### 3. Create Docker Network

```bash
docker network create gateway-network
```

### 4. Start Eureka Server

```bash
cd eurekaserver
./gradlew bootRun
```

The Eureka dashboard will be available at: http://localhost:8761

### 5. Start Product Service

Open a new terminal:

```bash
cd productservice
./gradlew bootRun
```

The service will start on port 8081 and register with Eureka.

### 6. Start Category Service

Open another terminal:

```bash
cd categoryservice
./gradlew bootRun
```

The service will start on port 8082 and register with Eureka.

### 7. Start Gateway Service

Open another terminal:

```bash
cd gateway
./gradlew bootRun
```

The Gateway will start on port 8888 and act as the main entry point.

## 📡 API Endpoints

### Gateway Routes

All requests should be sent to the gateway at `http://localhost:8888`

#### Product Endpoints
```
GET    http://localhost:8888/api/products           # Get all products
GET    http://localhost:8888/api/products/{id}      # Get product by ID
GET    http://localhost:8888/api/products/category/{categoryId}  # Get products by category
POST   http://localhost:8888/api/products           # Create product
PUT    http://localhost:8888/api/products/{id}      # Update product
DELETE http://localhost:8888/api/products/{id}      # Delete product
```

#### Category Endpoints
```
GET    http://localhost:8888/api/categories         # Get all categories
GET    http://localhost:8888/api/categories/{id}    # Get category by ID
POST   http://localhost:8888/api/categories         # Create category
PUT    http://localhost:8888/api/categories/{id}    # Update category
DELETE http://localhost:8888/api/categories/{id}    # Delete category
```

#### BFF Endpoint
```
GET    http://localhost:8888/bff/dashboard          # Get aggregated dashboard data
```

### Example Requests

#### Create a Category
```bash
curl -X POST http://localhost:8888/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and accessories"
  }'
```

#### Create a Product
```bash
curl -X POST http://localhost:8888/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "categoryId": 1
  }'
```

#### Get Dashboard Data (BFF)
```bash
curl http://localhost:8888/bff/dashboard
```

Response:
```json
{
  "products": [
    {
      "id": 1,
      "name": "Laptop",
      "description": "High-performance laptop",
      "price": 1299.99,
      "categoryId": 1
    }
  ],
  "categories": [
    {
      "id": 1,
      "name": "Electronics",
      "description": "Electronic devices and accessories"
    }
  ]
}
```

## 🔧 Configuration

### Gateway Configuration
The gateway is configured with the following routes in `gateway/src/main/java/co/istad/makara/gateway/config/GatewayConfig.java`:

- **Product Service Route**: `/api/products/**` → `http://localhost:8081`
- **Category Service Route**: `/api/categories/**` → `http://localhost:8082`
- **BFF Route**: `/bff/**` → Handled by BFFController
- **Next.js Route**: `/**` → `http://localhost:3000` (when frontend is implemented)

### Circuit Breaker Configuration
Circuit breaker settings in `gateway/src/main/resources/application.yaml`:

- **Sliding Window Size**: 10 requests
- **Minimum Number of Calls**: 5
- **Failure Rate Threshold**: 50%
- **Wait Duration in Open State**: 10 seconds

### Database Configuration
Each service connects to its own database:

**Product Service** (`productservice/src/main/resources/application.yaml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5555/product_db
    username: itpusr
    password: itp@168
```

**Category Service** (`categoryservice/src/main/resources/application.yaml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5555/category_db
    username: itpusr
    password: itp@168
```

## 🧪 Testing the Circuit Breaker

To test the circuit breaker functionality:

1. Stop the Product Service:
```bash
# Find and kill the process
ps aux | grep productservice
kill <PID>
```

2. Try to access products through the gateway:
```bash
curl http://localhost:8888/api/products
```

3. You should receive a fallback response from the circuit breaker.

4. Restart the Product Service and verify normal operation resumes.

## 🏃 Running All Services

For convenience, you can run all services using separate terminal windows:

```bash
# Terminal 1 - Database
cd postgres-db && docker-compose up

# Terminal 2 - Eureka
cd eurekaserver && ./gradlew bootRun

# Terminal 3 - Product Service
cd productservice && ./gradlew bootRun

# Terminal 4 - Category Service
cd categoryservice && ./gradlew bootRun

# Terminal 5 - Gateway
cd gateway && ./gradlew bootRun
```

Wait a few seconds between starting each service to ensure proper initialization and registration.

## 📊 Monitoring

### Eureka Dashboard
Access the Eureka Server dashboard to view registered services:
- URL: http://localhost:8761
- View service health and instances

### Application Logs
Each service outputs detailed logs including:
- Request routing information
- Circuit breaker state changes
- Database operations
- Service registration events

## 🔒 Security (Planned)

This project is titled with OAuth2, indicating planned security features:

- OAuth2 authentication and authorization (not yet implemented)
- JWT token-based authentication (planned)
- Secure service-to-service communication (planned)

## 🎨 Frontend Integration (Planned)

The gateway is configured to route to a Next.js frontend at `http://localhost:3000`. To integrate a frontend:

1. Create a Next.js application
2. Run it on port 3000
3. Make API calls to `http://localhost:8888/bff/dashboard` or individual service endpoints
4. The gateway will handle CORS and routing

## 📚 Learning Resources

This project demonstrates the following concepts:

- **Microservices Architecture**: Independent, loosely coupled services
- **API Gateway Pattern**: Single entry point for clients
- **Backend for Frontend (BFF)**: API aggregation for frontend optimization
- **Service Discovery**: Dynamic service registration and discovery
- **Circuit Breaker**: Fault tolerance and resilience
- **Database per Service**: Each microservice has its own database

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📝 License

This project is part of a learning exercise and is available for educational purposes.

## 👤 Author

**Makara Ren**
- GitHub: [@RenMakara](https://github.com/RenMakara)

## 🙏 Acknowledgments

- Spring Cloud team for excellent microservices tools
- Netflix OSS for Eureka service discovery
- Resilience4j for circuit breaker implementation

---

**Note**: This is an educational project demonstrating microservices architecture patterns. OAuth2 integration and Next.js frontend are planned for future implementation.
