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
    @RabbitListener(queues = "#{rabbitMqProperties.mobileQueueName}")
    public void handleMobileMessage(@Payload UserCreatedEvent userCreatedEvent,
                                    @Header("X-USER-ID") String userId) {
        log.info("Mobile message received. userId={}, payload={}", userId, userCreatedEvent);
    }

    @SneakyThrows
    @RabbitListener(queues = "#{rabbitMqProperties.desktopQueueName}")
    public void handleDesktopMessage(@Payload UserCreatedEvent userCreatedEvent,
                                     @Header("X-USER-ID") String userId) {
        log.info("Desktop message received. userId={}, payload={}", userId, userCreatedEvent);
    }

}
