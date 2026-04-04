package ourcorp.rabbitmqexample.consumer;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ourcorp.rabbitmqexample.model.event.UserCreatedEvent;
import ourcorp.rabbitmqexample.model.response.UserResponse;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserConsumerHandler {


    @SneakyThrows
    @RabbitListener(queues = "#{rabbitMqProperties.queueName}")
    public UserResponse handleMessage(@Payload UserCreatedEvent userCreatedEvent,
                                      @Header("X-USER-ID") String userId) {
        log.info("Received body: {}", userCreatedEvent);
        log.info("Received user id: {}", userId);
        return new UserResponse(userId, userCreatedEvent.getUsername());
    }

}
