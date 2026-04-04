package ourcorp.rabbitmqexample.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ourcorp.rabbitmqexample.config.properties.RabbitMqProperties;
import ourcorp.rabbitmqexample.model.event.UserCreatedEvent;
import ourcorp.rabbitmqexample.model.request.UserCreatedRequest;
import ourcorp.rabbitmqexample.model.response.UserResponse;
import ourcorp.rabbitmqexample.producer.api.UserProducer;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserProducerHandler implements UserProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final RabbitMqProperties rabbitMqProperties;


    @SneakyThrows
    @Override
    public void publish(UserCreatedRequest request) {

        var message = MessageBuilder
                .withBody(objectMapper.writeValueAsBytes(new UserCreatedEvent(request.getUsername())))
                .setHeader("X-USER-ID", request.getId())
                .setCorrelationId(UUID.randomUUID().toString())
                .setContentType("application/json")
                .build();

        var receivedMessage = rabbitTemplate.sendAndReceive(
                rabbitMqProperties.getExchangeName(),
                rabbitMqProperties.getRoutingKeyName(),
                message
        );

        if (receivedMessage != null) {
            UserResponse response = objectMapper.readValue(receivedMessage.getBody(), UserResponse.class);
            log.info("Received response from consumer: {}", response);
        } else {
            log.warn("No response received from consumer (timeout or consumer did not reply)");
        }


    }

}
