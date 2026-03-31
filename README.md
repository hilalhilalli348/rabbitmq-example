> Language / Dil: [AZ](#az) | [EN](#en)

## AZ

# RabbitMQ Example

Bu repo RabbitMQ ilə bağlı sadə nümunələri göstərən demo layihədir. Məqsəd producer, consumer və queue/exchange məntiqini praktik formada göstərməkdir.

Repo daxilində nümunələr branch-lər üzrə ayrılıb. Hər branch ayrıca bir RabbitMQ ssenarisini göstərir.

- exchange növləri üçün ayrıca nümunələr var
- `retry` və `DLQ` mövzuları üçün ayrıca nümunələr var
- hazırkı branch `header-exchange` ssenarisini göstərir

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## Bu Branch Nə Göstərir

Bu branch `header exchange` nümunəsidir.

Axın belədir:

1. Client `POST /api/v1/users` endpoint-inə request göndərir
2. tətbiq `UserCreatedEvent` yaradır
3. message `user.events.header` exchange-inə göndərilir
4. message daxilində `user-created=true` və `user-authorized=true` header-ləri yazılır
5. `mobile.notification.user-created` və `desktop.notification.user-created` queue-ları exchange-ə həmin header şərtləri ilə bind olunur
6. header-lər şərtə uyğun gəldiyi üçün hər iki queue message-i qəbul edir
7. mobile və desktop consumer-lər message-i ayrıca emal edir

## RabbitMQ Konfiqurasiyası

- Exchange: `user.events.header`
- Headers:
  - `user-created=true`
  - `user-authorized=true`
- Mobile queue: `mobile.notification.user-created`
- Desktop queue: `desktop.notification.user-created`
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

- header şərtləri uyğun olduğu üçün message həm mobile, həm desktop queue-larına çatır
- hər iki consumer message body-ni ayrıca qəbul edir
- `X-USER-ID` header-i log-larda görünür

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
- the current branch demonstrates the `header-exchange` scenario

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## What This Branch Shows

This branch demonstrates a `headers exchange` example.

Flow:

1. The client sends a request to `POST /api/v1/users`
2. the application creates a `UserCreatedEvent`
3. the message is published to `user.events.header`
4. the message includes the `user-created=true` and `user-authorized=true` headers
5. the `mobile.notification.user-created` and `desktop.notification.user-created` queues are bound with those header conditions
6. both queues receive the message because the headers match
7. the mobile and desktop consumers process the message separately

## RabbitMQ Configuration

- Exchange: `user.events.header`
- Headers:
  - `user-created=true`
  - `user-authorized=true`
- Mobile queue: `mobile.notification.user-created`
- Desktop queue: `desktop.notification.user-created`
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

- the message reaches both the mobile and desktop queues because the headers match
- both consumers receive the message body separately
- the `X-USER-ID` header appears in logs

## RabbitMQ UI

- URL: [http://localhost:15672](http://localhost:15672)
- username: `guest`
- password: `guest`
