package ourcorp.rabbitmqexample.config;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ourcorp.rabbitmqexample.config.properties.RabbitMqProperties;

@RequiredArgsConstructor
@Configuration
public class RabbitmqConfig {

    private final RabbitMqProperties rabbitMqProperties;

    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(rabbitMqProperties.getMainQueueName())
                .build();
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(rabbitMqProperties.getRetryQueueName())
                .deadLetterRoutingKey(rabbitMqProperties.getMainRoutingKeyName())
                .deadLetterExchange(rabbitMqProperties.getMainExchangeName())
                .ttl(rabbitMqProperties.getRetryTtlMs())
                .build();
    }

    @Bean
    public Queue dlqQueue() {
        return new Queue(rabbitMqProperties.getDlqQueueName());
    }

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(rabbitMqProperties.getMainExchangeName());
    }

    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange(rabbitMqProperties.getRetryExchangeName());
    }

    @Bean
    public DirectExchange dlqExchange() {
        return new DirectExchange(rabbitMqProperties.getDlqExchangeName());
    }

    @Bean
    public Binding mainQueueBinding() {
        return BindingBuilder.bind(mainQueue())
                .to(mainExchange())
                .with(rabbitMqProperties.getMainRoutingKeyName());
    }

    @Bean
    public Binding retryQueueBinding() {
        return BindingBuilder.bind(retryQueue())
                .to(retryExchange())
                .with(rabbitMqProperties.getRetryRoutingKeyName());
    }

    @Bean
    public Binding dlqQueueBinding() {
        return BindingBuilder.bind(dlqQueue())
                .to(dlqExchange())
                .with(rabbitMqProperties.getDlqRoutingKeyName());
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
