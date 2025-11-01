# Microservice with Message Queue

Micro_MQ is a minimalist Java-based messaging queue for microservice architectures. It enables asynchronous communication between services, decoupling producers and consumers and helping systems scale and remain resilient.

## Features

- Lightweight core for message buffering and delivery  
- Decoupled communication (producers & consumers do not depend on each other)  
- Supports multiple microservices (facade, logging, messages, etc.)  
- Simple configuration and integration with Spring / Java stacks  

## Why Micro_MQ?

In microservices, synchronous HTTP calls lead to tight coupling and cascading failures. A message queue lets services communicate asynchronously — if one service is down, messages wait in the queue until it comes back up. It helps with scalability, reliability, and smoothing workloads. :contentReference[oaicite:0]{index=0}

## Project Structure

- `facade-service`: accepts HTTP requests and enqueues jobs/messages  
- `messages-service`: consumes messages to send notifications  
- `logging-service`: consumes log events or metrics  

## Getting Started

### Prerequisites

- Java 11+  
- Maven  
- A running instance of the message broker (you can use this project’s built-in queue or adapt to RabbitMQ/Kafka)

### Usage

- Build and start each service in Docker

### Testing

Use `Requests.http` to send sample requests (e.g. POST to the facade service). You should observe messages being enqueued and consumed by downstream services.

## Configuration

Configurations are managed via `application.yml` or `application.properties` files in each service. Typical settings include:
* broker host / port
* queue names
* retry policies
* consumer threading
