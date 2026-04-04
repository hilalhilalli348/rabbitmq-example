> Language / Dil: [AZ](#az) | [EN](#en)

## AZ

# RabbitMQ Example

Bu repo RabbitMQ ilə bağlı sadə nümunələri göstərən demo layihədir. Məqsəd producer, consumer və queue/exchange məntiqini praktik formada göstərməkdir.

Repo daxilində nümunələr branch-lər üzrə ayrılıb. Hər branch ayrıca bir RabbitMQ ssenarisini göstərir.

- exchange növləri üçün ayrıca nümunələr var
- `retry` və `DLQ` mövzuları üçün ayrıca nümunələr var
- hazırkı branch `default-exchange` ssenarisini göstərir

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## Bu Branch Nə Göstərir

Bu branch RabbitMQ `default exchange` nümunəsini göstərir.

Axın belədir:

1. Client `POST /api/v1/users` endpoint-inə request göndərir
2. tətbiq `UserCreatedEvent` yaradır və `X-USER-ID` header-i əlavə edir
3. producer message-i default exchange-ə, yəni `""` exchange adına göndərir
4. routing key kimi queue adı `notification.user-created` istifadə olunur
5. RabbitMQ message-i uyğun queue-ya route edir
6. consumer message-i qəbul edir və log-a yazır

## RabbitMQ Konfiqurasiyası

- Exchange: `""` (default exchange)
- Routing key: `notification.user-created`
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

- producer message-i RabbitMQ-ya göndərir
- consumer message body-ni qəbul edir
- `X-USER-ID` header-i log-larda görünür

## Qeyd

- bu nümunədə ayrıca exchange bean və binding yaradılmır
- default exchange RabbitMQ-da daxili mövcuddur və queue adı ilə route edir
- producer `rabbitTemplate.send("", queueName, message)` çağırışı ilə message göndərir

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
- the current branch demonstrates the `default-exchange` scenario

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## What This Branch Shows

This branch demonstrates the RabbitMQ `default exchange` example.

Flow:

1. The client sends a request to `POST /api/v1/users`
2. the application creates a `UserCreatedEvent` and adds the `X-USER-ID` header
3. the producer sends the message to the default exchange, which is the `""` exchange name
4. the queue name `notification.user-created` is used as the routing key
5. RabbitMQ routes the message to the matching queue
6. the consumer receives the message and writes it to logs

## RabbitMQ Configuration

- Exchange: `""` (default exchange)
- Routing key: `notification.user-created`
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

- the producer sends the message to RabbitMQ
- the consumer receives the message body
- the `X-USER-ID` header appears in logs

## Note

- this example does not define a separate exchange bean or binding
- the default exchange already exists in RabbitMQ and routes by queue name
- the producer sends the message with `rabbitTemplate.send("", queueName, message)`

## RabbitMQ UI

- URL: [http://localhost:15672](http://localhost:15672)
- username: `guest`
- password: `guest`
