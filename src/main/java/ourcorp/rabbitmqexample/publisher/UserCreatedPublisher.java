package ourcorp.rabbitmqexample.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ourcorp.rabbitmqexample.config.RabbitmqConfig;
import ourcorp.rabbitmqexample.model.event.UserCreatedEvent;
import ourcorp.rabbitmqexample.model.request.UserCreatedRequest;

@RequiredArgsConstructor
@Component
public class UserCreatedPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;


    @SneakyThrows
    public void run(UserCreatedRequest request) {

        var msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(new UserCreatedEvent(request.getUsername())))
                .setHeader("X-USER-ID", UUID.randomUUID())
                .build();

        rabbitTemplate.send(
                RabbitmqConfig.USER_CREATED_EXCHANGE_NAME,
                RabbitmqConfig.USER_CREATED_ROUTING_KEY_NAME,
                msg
        );

    }

}
