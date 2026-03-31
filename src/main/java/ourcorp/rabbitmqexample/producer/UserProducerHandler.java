package ourcorp.rabbitmqexample.producer;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ourcorp.rabbitmqexample.config.properties.RabbitMqProperties;
import ourcorp.rabbitmqexample.model.request.UserCreatedRequest;
import ourcorp.rabbitmqexample.producer.api.UserProducer;

@RequiredArgsConstructor
@Component
public class UserProducerHandler implements UserProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;


    @SneakyThrows
    @Override
    public void publish(UserCreatedRequest request) {

    }

}
