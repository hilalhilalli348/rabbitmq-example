package ourcorp.rabbitmqexample.config;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ourcorp.rabbitmqexample.config.properties.RabbitMqProperties;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RabbitmqConfig {

    public final RabbitMqProperties rabbitMqProperties;

    @Bean
    public Queue queue() {
        return new Queue(rabbitMqProperties.getQueueName());
    }

    @Bean
    public DirectExchange createExchange() {
        return new DirectExchange(rabbitMqProperties.getExchangeName());
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(createExchange())
                .with(rabbitMqProperties.getRoutingKeyName());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // Correlated publisher confirm callback
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                log.info("Message successfully delivered to broker, correlationId={}",
                        correlationData != null ? correlationData.getId() : "null");
            } else {
                log.error("Message failed to deliver to broker: {}", cause);
            }
        });

        // Returned message callback for unroutable messages
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(
                returned -> log.error("Message unroutable: replyCode={}, replyText={}, exchange={}, routingKey={}",
                        returned.getReplyCode(), returned.getReplyText(),
                        returned.getExchange(), returned.getRoutingKey()));

        return rabbitTemplate;
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
