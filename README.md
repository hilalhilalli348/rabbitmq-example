> Language / Dil: [AZ](#az) | [EN](#en)

## AZ

# RabbitMQ Example

Bu repo RabbitMQ ilə bağlı sadə nümunələri göstərən demo layihədir. Məqsəd producer, consumer və queue/exchange məntiqini praktik formada göstərməkdir.

Repo daxilində nümunələr branch-lər üzrə ayrılıb. Hər branch ayrıca bir RabbitMQ ssenarisini göstərir.

- exchange növləri üçün ayrıca nümunələr var
- `retry` və `DLQ` mövzuları üçün ayrıca nümunələr var
- hazırkı branch `topic-exchange` ssenarisini göstərir

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## Bu Branch Nə Göstərir

Bu branch `topic exchange` nümunəsidir.

Axın belədir:

1. Client `POST /api/v1/users` endpoint-inə request göndərir
2. tətbiq `UserCreatedEvent` yaradır
3. message `user.events.topic` exchange-inə `user.created` routing key ilə göndərilir
4. queue `user.*` pattern-i ilə exchange-ə bind olunur
5. `notification.user-created` queue-su uyğun message-i qəbul edir
6. consumer message-i log-a yazır

## RabbitMQ Konfiqurasiyası

- Exchange: `user.events.topic`
- Pattern: `user.*`
- Routing key: `user.created`
- Queue: `notification.user-created`
- RabbitMQ port: `5672`
- RabbitMQ UI port: `15672`
- Application port: `8080`

## Necə İşə Salmaq Olar

### 1. RabbitMQ-nu başladın

```bash
docker compose up -d
```

### 2. Tətbiqi başladın

```bash
./gradlew bootRun
```

## Test Request

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"id":1,"username":"hilal"}'
```

## Gözlənilən Nəticə

- `user.created` routing key-i `user.*` pattern-ə uyğun gəldiyi üçün message queue-ya çatır
- consumer message body-ni qəbul edir və log-a yazır

## RabbitMQ UI

- URL: [http://localhost:15672](http://localhost:15672)
- username: `guest`
- password: `guest`

## EN

# RabbitMQ Example

This repo is a simple demo project for RabbitMQ examples. Its purpose is to show producer, consumer, queue, and exchange behavior in a practical way.

Examples in this repository are separated by branches. Each branch demonstrates a different RabbitMQ scenario.

- separate examples exist for exchange types
- separate examples exist for `retry` and `DLQ`
- the current branch demonstrates the `topic-exchange` scenario

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## What This Branch Shows

This branch demonstrates a `topic exchange` example.

Flow:

1. The client sends a request to `POST /api/v1/users`
2. the application creates a `UserCreatedEvent`
3. the message is published to `user.events.topic` with the `user.created` routing key
4. the queue is bound to the exchange with the `user.*` pattern
5. the `notification.user-created` queue receives the matching message
6. the consumer writes the message to logs

## RabbitMQ Configuration

- Exchange: `user.events.topic`
- Pattern: `user.*`
- Routing key: `user.created`
- Queue: `notification.user-created`
- RabbitMQ port: `5672`
- RabbitMQ UI port: `15672`
- Application port: `8080`

## How To Run

### 1. Start RabbitMQ

```bash
docker compose up -d
```

### 2. Start the application

```bash
./gradlew bootRun
```

## Test Request

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"id":1,"username":"hilal"}'
```

## Expected Result

- the message reaches the queue because `user.created` matches the `user.*` pattern
- the consumer receives the message body and writes it to logs

## RabbitMQ UI

- URL: [http://localhost:15672](http://localhost:15672)
- username: `guest`
- password: `guest`
