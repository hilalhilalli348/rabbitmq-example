package ourcorp.rabbitmqexample.config;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ourcorp.rabbitmqexample.config.properties.RabbitMqProperties;

@RequiredArgsConstructor
@Configuration
public class RabbitmqConfig {

    public final RabbitMqProperties rabbitMqProperties;

    @Bean
    public Queue firstQueue() {
        return new Queue(rabbitMqProperties.getFirstQueueName());
    }

    @Bean
    public Queue secondQueue() {
        return new Queue(rabbitMqProperties.getSecondQueueName());
    }

    @Bean
    public Exchange createConsistentHashExchange() {
        return new CustomExchange(
                rabbitMqProperties.getExchangeName(),
                "x-consistent-hash",
                false,  // durable: false
                false,  // autoDelete: false
                Map.of() // Hashing will be performed based on the routing key
        );
    }

    @Bean
    public Binding firstQueueBinding() {
        return BindingBuilder.bind(firstQueue())
                .to(createConsistentHashExchange())
                .with("1")
                .noargs();
    }

    @Bean
    public Binding secondQueueBinding() {
        return BindingBuilder.bind(secondQueue())
                .to(createConsistentHashExchange())
                .with("1")
                .noargs();
    }

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(NON_NULL);
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper());
    }

}
