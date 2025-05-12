
# Event Processing Backend System

## Overview

This project is a scalable backend system designed to process events asynchronously. The system simulates event creation and processing, integrates with a message queue (RabbitMQ), uses caching (Redis), and supports horizontal scaling via load balancing with NGINX.

Key features include:

* Event ingestion and processing
* Asynchronous message queue handling
* Redis caching for event storage and retrieval
* Horizontal scaling with multiple Spring Boot backend instances
* Load balancing using NGINX
* Monitoring with Spring Boot Actuator

---

## **System Architecture**

The system consists of several major components:

1. **Spring Boot Application**: The backend of the system for processing events.
2. **RabbitMQ**: A message queue for handling asynchronous event processing.
3. **Redis**: Used for caching event data for quick access.
4. **NGINX**: A reverse proxy/load balancer for distributing requests across multiple backend instances.
5. **Spring Boot Actuator**: For application health checks, metrics, and monitoring.
6. **PostgreSQL (Optional)**: For event storage if required for persistent storage.

---

## **Tech Stack**

* **Backend**: Spring Boot (Java)
* **Queue**: RabbitMQ
* **Cache**: Redis
* **Database**: PostgreSQL (optional)
* **Load Balancer**: NGINX
* **Monitoring**: Spring Boot Actuator
* **Containerization**: Docker

---

## **Getting Started**

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/event-processing-backend.git
cd event-processing-backend
```

### 2. Requirements

Ensure you have the following tools installed:

* **Docker**: For containerization
* **Docker Compose**: To orchestrate multi-container setups
* **Redis**: Local Redis instance or Docker setup
* **RabbitMQ**: Local RabbitMQ instance or Docker setup
* **NGINX**: To manage load balancing (configured with Docker)
* **Java 17**: For Spring Boot applications

### 3. Build the Spring Boot Application

Build the Spring Boot project with Maven:

```bash
mvn clean install
```

This will generate a `JAR` file that will be used in Docker.

### 4. Docker Setup

#### Dockerfile for Spring Boot Application

This `Dockerfile` creates a Docker image for your Spring Boot backend.

```dockerfile
# Use OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file to container
COPY target/event_processing-0.0.1-SNAPSHOT.jar /app/event_processing.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "event_processing.jar"]
```

#### Docker Compose Configuration

The `docker-compose.yml` file defines the services for:

* **Backend (Spring Boot)**: Scaled to 3 instances for horizontal scaling.
* **Redis**: Caching layer for event storage.
* **NGINX**: Load balancer to distribute traffic to backend services.

```yaml
version: '3.8'

services:
  backend:
    image: event_processing:latest
    build: .
    container_name: event_processing_backend
    ports:
      - "8080"
    environment:
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379
    depends_on:
      - redis
    deploy:
      replicas: 3
      restart_policy:
        condition: on-failure

  redis:
    image: redis:alpine
    container_name: redis
    ports:
      - "6379:6379"
    restart: always

  nginx:
    image: nginx:latest
    container_name: nginx
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
    depends_on:
      - backend
```

#### NGINX Configuration for Load Balancing

NGINX is configured to balance traffic across three backend Spring Boot instances using **round-robin** load balancing.

```nginx
http {
    upstream backend {
        server backend1:8080;
        server backend2:8080;
        server backend3:8080;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://backend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
    }
}
```

---

## **Running the Application**

### 1. Build and Start the Containers

To build and start the Docker containers, use:

```bash
docker-compose up --build
```

This will:

* Build and run the Spring Boot application
* Start Redis and NGINX containers
* Expose the application on port 8080 and NGINX on port 80

### 2. Verify the System is Running

* **Backend API**: Open your browser and go to [http://localhost:8080](http://localhost:8080).
* **NGINX**: Access the load balanced API via [http://localhost](http://localhost). NGINX will distribute the traffic between the backend services.
* **Redis**: Connect to Redis using `redis-cli` or a Redis GUI tool (localhost:6379) to inspect cached data.
* **RabbitMQ**: Make sure RabbitMQ is running for the event queue.

---

## **Monitoring**

* **Spring Boot Actuator**: Access the application health and metrics at:

    * [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
    * [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics)

### Example Metrics:

* JVM memory usage
* CPU usage
* Event processing count
* Redis connection status

---

## **Testing the Application**

You can test the event ingestion and processing with **Postman** or **Swagger UI**:

* **Postman**:

    * POST `/api/events` to create new events.
    * GET `/api/events/{id}` to retrieve events from cache or DB.

---

## **Scaling the Application**

To scale the backend services (e.g., increase the number of Spring Boot instances), run:

```bash
docker-compose up --scale backend=5
```

This will scale the backend to 5 instances, and NGINX will automatically distribute traffic across all backend containers.

---

## **Production-Ready Configuration**

For production deployment, consider the following:

* **Persistent Redis storage** for data durability.
* **Prometheus/Grafana** for advanced metrics and monitoring.
* **API rate limiting** to prevent overload.
* **Database read replicas** for enhanced scalability.

---

## **Troubleshooting**

If you encounter issues:

* **Logs**: Check logs with `docker logs container_name`.
* **Health Checks**: Verify Redis and RabbitMQ connections through Actuator endpoints.
* **NGINX Logs**: Check the logs for traffic distribution or errors with `docker logs nginx`.

---
