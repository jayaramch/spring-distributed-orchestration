# My Spring Boot Application

This is a Spring Boot application that demonstrates basic functionality such as REST APIs, Kafka integration, and scheduled tasks.

This command would start 4 pods with 9090, 9091, 9092, 9093
http://localhost:8080/orchestrate/start

Then opend command prompt

## 📦 Project Structure

## Docker - we should start Kafka with Docker compose

create a docker-compose.yml file and start running below commands to run kafka & Zookeeper.

```yaml
version: '3.8'

services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.6.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:7.6.0
    ports:
      - "6092:6092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://host.docker.internal:6092
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:6092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    depends_on:
      - zookeeper
  kafka-ui:
    image: provectuslabs/kafka-ui
    ports:
      - "7080:7080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:6092
      KAFKA_CLUSTERS_0_ZOOKEEPER: zookeeper:2181
    depends_on:
      - kafka
```
This would start Kafka and zookeper with localhost 6092 and 2181 ports

## Docker commands

docker-compose up -d /n
docker-compose down 

docker exec -it kafka-kafka-1 bash

## Docker Kafka commands to create topic

kafka-topics --bootstrap-server localhost:6092 --list

kafka-topics --bootstrap-server localhost:6092 --create --topic test-topic --partitions 1 --replication-factor 1

## Kafka springboot configuration
Pls refer application.yml file

## Testing
Should kill one of the Microservice POD and see whether our PUB-SUB is handling the self healing

netstat -ano | findstr 9090
taskkill /PID <PROCEDD_ID> /F


