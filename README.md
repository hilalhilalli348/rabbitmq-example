> Language / Dil: [AZ](#az) | [EN](#en)

## AZ

# RabbitMQ Example

Bu repo RabbitMQ ilə bağlı sadə nümunələri göstərən demo layihədir. Məqsəd producer, consumer və queue/exchange məntiqini praktik formada göstərməkdir.

Repo daxilində nümunələr branch-lər üzrə ayrılıb. Hər branch ayrıca bir RabbitMQ ssenarisini göstərir.

- exchange növləri üçün ayrıca nümunələr var
- `retry` və `DLQ` mövzuları üçün ayrıca nümunələr var
- hazırkı branch `consistent-hash-exchange` ssenarisini göstərir

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## Bu Branch Nə Göstərir

Bu branch `consistent hash exchange` nümunəsidir.

Axın belədir:

1. Client `POST /api/v1/users` endpoint-inə request göndərir
2. tətbiq `UserCreatedEvent` yaradır
3. message `user.events.consistent` exchange-inə göndərilir
4. hashing üçün routing key kimi `userId` istifadə olunur
5. `first.notification.user-created` və `second.notification.user-created` queue-ları exchange-ə eyni weight ilə bind olunur
6. consistent hash exchange routing key-ə görə message-i queue-lardan yalnız birinə göndərir
7. eyni hash dəyəri üçün message eyni queue-ya düşür və consumer onu emal edir

## RabbitMQ Konfiqurasiyası

- Exchange: `user.events.consistent`
- Exchange type: `x-consistent-hash`
- Hash input: `userId` routing key-i
- First queue: `first.notification.user-created`
- Second queue: `second.notification.user-created`
- Binding weight:
  - first queue: `1`
  - second queue: `1`
- RabbitMQ port: `5672`
- RabbitMQ UI port: `15672`
- Application port: `8080`

## Qeyd

Bu nümunə `x-consistent-hash` exchange istifadə edir. RabbitMQ-da consistent hash exchange plugin aktiv olmalıdır.

Bu branch-də hash routing key üzərindən aparılır. Biz `userId` dəyərini routing key kimi göndərmişik və paylanma buna əsasən edilir.

Alternativ olaraq consistent hash exchange header üzərindən də işləyə bilər. Belə ssenaridə exchange yaradılarkən uyğun argument verilir və hash routing key əvəzinə seçilmiş header dəyərinə görə hesablanır.

Header əsaslı nümunə:

```java
@Bean
public Exchange createConsistentHashExchange() {
    return new CustomExchange(
            "user.events.consistent", // exchange adı
            "x-consistent-hash", // exchange tipi
            false, // durable deyil
            false, // auto-delete deyil
            Map.of("hash-header", "X-USER-ID") // hash bu header üzərindən hesablanır
    );
}
```

Bu halda producer message göndərərkən `X-USER-ID` header-ni set etməlidir və routing key boş qala bilər.

Sadə producer nümunəsi:

```java
var message = MessageBuilder
        .withBody(objectMapper.writeValueAsBytes(new UserCreatedEvent(request.getUsername())))
        .setHeader("X-USER-ID", request.getId()) // hash bu header dəyərinə görə hesablanır
        .setCorrelationId(UUID.randomUUID().toString())
        .setContentType("application/json")
        .build();

rabbitTemplate.send(
        rabbitMqProperties.getExchangeName(),
        "",
        message
);
```

Plugin-i aktiv etmək üçün:

```bash
docker exec -it rabbitmq rabbitmq-plugins enable rabbitmq_consistent_hash_exchange
```

Sonra RabbitMQ-nu restart etmək üçün:

```bash
docker restart rabbitmq
```

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

- eyni `userId` üçün message-lər eyni queue-ya route olunur
- message hər dəfə yalnız bir queue tərəfindən qəbul olunur
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
- the current branch demonstrates the `consistent-hash-exchange` scenario

## Tech Stack

- Java 21
- Spring Boot 3.5.13
- Spring AMQP
- RabbitMQ
- Docker Compose

## What This Branch Shows

This branch demonstrates a `consistent hash exchange` example.

Flow:

1. The client sends a request to `POST /api/v1/users`
2. the application creates a `UserCreatedEvent`
3. the message is published to `user.events.consistent`
4. the `userId` is used as the routing key for hashing
5. the `first.notification.user-created` and `second.notification.user-created` queues are bound to the exchange with the same weight
6. the consistent hash exchange routes the message to only one queue based on the routing key
7. messages with the same hash input are routed to the same queue and processed by its consumer

## RabbitMQ Configuration

- Exchange: `user.events.consistent`
- Exchange type: `x-consistent-hash`
- Hash input: `userId` routing key
- First queue: `first.notification.user-created`
- Second queue: `second.notification.user-created`
- Binding weight:
  - first queue: `1`
  - second queue: `1`
- RabbitMQ port: `5672`
- RabbitMQ UI port: `15672`
- Application port: `8080`

## Note

This example uses the `x-consistent-hash` exchange type. The RabbitMQ consistent hash exchange plugin must be enabled.

In this branch, hashing is based on the routing key. We send the `userId` as the routing key, and message distribution is calculated from that value.

As an alternative, the consistent hash exchange can also hash by a message header. In that setup, the exchange is declared with the appropriate argument, and hashing is calculated from the selected header value instead of the routing key.

Header-based example:

```java
@Bean
public Exchange createConsistentHashExchange() {
    return new CustomExchange(
            "user.events.consistent", // exchange name
            "x-consistent-hash", // exchange type
            false, // not durable
            false, // not auto-delete
            Map.of("hash-header", "X-USER-ID") // hash is calculated from this header
    );
}
```

In that case, the producer should set the `X-USER-ID` header on the message, and the routing key can be left empty.

Simple producer example:

```java
var message = MessageBuilder
        .withBody(objectMapper.writeValueAsBytes(new UserCreatedEvent(request.getUsername())))
        .setHeader("X-USER-ID", request.getId()) // hash is calculated from this header
        .setCorrelationId(UUID.randomUUID().toString())
        .setContentType("application/json")
        .build();

rabbitTemplate.send(
        rabbitMqProperties.getExchangeName(),
        "",
        message
);
```

To enable the plugin:

```bash
docker exec -it rabbitmq rabbitmq-plugins enable rabbitmq_consistent_hash_exchange
```

Then restart RabbitMQ:

```bash
docker restart rabbitmq
```

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

- messages with the same `userId` are routed to the same queue
- each message is consumed by only one queue at a time
- the `X-USER-ID` header appears in logs

## RabbitMQ UI

- URL: [http://localhost:15672](http://localhost:15672)
- username: `guest`
- password: `guest`
