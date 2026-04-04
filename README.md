> Language / Dil: [AZ](#az) | [EN](#en)

## AZ

# RabbitMQ Example

Bu repo RabbitMQ ilə bağlı sadə nümunələri göstərən demo layihədir. Məqsəd producer, consumer və queue/exchange məntiqini praktik formada göstərməkdir.

Repo daxilində nümunələr branch-lər üzrə ayrılıb. Hər branch ayrıca bir RabbitMQ ssenarisini göstərir.

- exchange növləri üçün ayrıca nümunələr var
- `retry` və `DLQ` mövzuları üçün ayrıca nümunələr var
- hazırkı branch `synchronous-request-response-messaging` ssenarisini göstərir

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## Bu Branch Nə Göstərir

Bu branch RabbitMQ üzərindən `synchronous request-response messaging` nümunəsini göstərir.

Axın belədir:

1. Client `POST /api/v1/users` endpoint-inə request göndərir
2. tətbiq request-dən `UserCreatedEvent` yaradır və `X-USER-ID` header-i əlavə edir
3. producer `RabbitTemplate.sendAndReceive(...)` ilə message-i `user.events.direct` exchange-inə `user.created` routing key ilə göndərir
4. message `notification.user-created` queue-suna route olunur
5. consumer request message-i qəbul edir və `UserResponse` reply qaytarır
6. producer reply message-i alır və response-u log-a yazır

Bu nümunədə HTTP endpoint birbaşa response qaytarmır; response RabbitMQ reply mexanizmi ilə producer tərəfində qəbul olunur və log-lanır.

## RabbitMQ Konfiqurasiyası

- Exchange: `user.events.direct`
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

- producer request message-i RabbitMQ-ya göndərir
- consumer `UserCreatedEvent` və `X-USER-ID` dəyərini qəbul edir
- consumer `UserResponse` reply qaytarır
- producer həmin reply-ni qəbul edib log-a yazır

## Qeyd

- reply axınının işləməsi üçün `UserConsumerHandler` daxilindəki `@RabbitListener` metodu aktiv olmalıdır
- consumer reply qaytarmasa, producer tərəfdə timeout və ya `No response received from consumer` log-u görünəcək

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
- the current branch demonstrates the `synchronous-request-response-messaging` scenario

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## What This Branch Shows

This branch demonstrates `synchronous request-response messaging` over RabbitMQ.

Flow:

1. The client sends a request to `POST /api/v1/users`
2. the application creates a `UserCreatedEvent` from the request and adds the `X-USER-ID` header
3. the producer sends the message with `RabbitTemplate.sendAndReceive(...)` to `user.events.direct` using the `user.created` routing key
4. the message is routed to the `notification.user-created` queue
5. the consumer receives the request message and returns a `UserResponse`
6. the producer receives the reply message and logs the response

In this example, the HTTP endpoint does not directly return the response; the response is received on the producer side through RabbitMQ's reply mechanism and written to logs.

## RabbitMQ Configuration

- Exchange: `user.events.direct`
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

- the producer sends a request message to RabbitMQ
- the consumer receives the `UserCreatedEvent` payload and `X-USER-ID` value
- the consumer returns a `UserResponse` reply
- the producer receives that reply and writes it to logs

## Note

- for the reply flow to work, the `@RabbitListener` method in `UserConsumerHandler` must be enabled
- if the consumer does not return a reply, the producer will log a timeout or `No response received from consumer`

## RabbitMQ UI

- URL: [http://localhost:15672](http://localhost:15672)
- username: `guest`
- password: `guest`
