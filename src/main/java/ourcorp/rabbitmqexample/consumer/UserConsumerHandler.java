package ourcorp.rabbitmqexample.consumer;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ourcorp.rabbitmqexample.model.event.UserCreatedEvent;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserConsumerHandler {


    @SneakyThrows
    @RabbitListener(queues = "#{rabbitMqProperties.firstQueueName}")
    public void handleFirstQueueMessage(@Payload UserCreatedEvent userCreatedEvent,
                                        @Header("X-USER-ID") String userId) {
        log.info("F.Q message received. userId={}, payload={}", userId, userCreatedEvent);
    }

    @SneakyThrows
    @RabbitListener(queues = "#{rabbitMqProperties.secondQueueName}")
    public void handleSecondQueueMessage(@Payload UserCreatedEvent userCreatedEvent,
                                         @Header("X-USER-ID") String userId) {
        log.info("S.Q message received. userId={}, payload={}", userId, userCreatedEvent);
    }

}
