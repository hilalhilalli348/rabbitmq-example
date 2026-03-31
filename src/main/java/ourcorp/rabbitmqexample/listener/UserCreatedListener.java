package ourcorp.rabbitmqexample.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ourcorp.rabbitmqexample.config.RabbitmqConfig;
import ourcorp.rabbitmqexample.model.event.UserCreatedEvent;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserCreatedListener {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @RabbitListener(queues = RabbitmqConfig.USER_CREATED_QUEUE_NAME)
    public void handleMessage(Message message) {
        log.info("Received body: {}", objectMapper.readValue(message.getBody(), UserCreatedEvent.class));
        log.info("Received properties: {}", message.getMessageProperties());
    }

}
