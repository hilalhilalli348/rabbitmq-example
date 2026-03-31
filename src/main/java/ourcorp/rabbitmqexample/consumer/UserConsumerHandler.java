package ourcorp.rabbitmqexample.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper;

//    @SneakyThrows
//    @RabbitListener(queues = "#{rabbitMqProperties.queueName}")
//    public void handleMessage( ) {
//
//    }

}
