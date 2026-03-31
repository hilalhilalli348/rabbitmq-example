> Language / Dil: [AZ](#az) | [EN](#en)

## AZ

# RabbitMQ Example

Bu repo RabbitMQ ilə bağlı sadə nümunələri göstərən demo layihədir. Məqsəd producer, consumer və queue/exchange məntiqini praktik formada göstərməkdir.

Repo daxilində nümunələr branch-lər üzrə ayrılıb. Hər branch ayrıca bir RabbitMQ ssenarisini göstərir.

- exchange növləri üçün ayrıca nümunələr var
- `retry` və `DLQ` mövzuları üçün ayrıca nümunələr var
- hazırkı branch `fanout-exchange` ssenarisini göstərir

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## Bu Branch Nə Göstərir

Bu branch `fanout exchange` nümunəsidir.

Axın belədir:

1. Client `POST /api/v1/users` endpoint-inə request göndərir
2. tətbiq `UserCreatedEvent` yaradır
3. message `user.events.fanout` exchange-inə göndərilir
4. `mobile.notification.user-created` və `desktop.notification.user-created` queue-ları exchange-ə bind olunur
5. fanout exchange eyni message-i bütün bağlı queue-lara göndərir
6. mobile və desktop consumer-lər message-i ayrıca qəbul edir

## RabbitMQ Konfiqurasiyası

- Exchange: `user.events.fanout`
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

- eyni message həm mobile, həm desktop queue-larına çatır
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
- the current branch demonstrates the `fanout-exchange` scenario

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## What This Branch Shows

This branch demonstrates a `fanout exchange` example.

Flow:

1. The client sends a request to `POST /api/v1/users`
2. the application creates a `UserCreatedEvent`
3. the message is published to `user.events.fanout`
4. the `mobile.notification.user-created` and `desktop.notification.user-created` queues are bound to the exchange
5. the fanout exchange broadcasts the same message to all bound queues
6. the mobile and desktop consumers receive the message separately

## RabbitMQ Configuration

- Exchange: `user.events.fanout`
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

- the same message reaches both the mobile and desktop queues
- both consumers receive the message body separately
- the `X-USER-ID` header appears in logs

## RabbitMQ UI

- URL: [http://localhost:15672](http://localhost:15672)
- username: `guest`
- password: `guest`
