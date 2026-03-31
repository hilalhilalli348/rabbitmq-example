package ourcorp.rabbitmqexample.config;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
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
    public Queue desktopQueue() {
        return new Queue(rabbitMqProperties.getDesktopQueueName());
    }

    @Bean
    public Queue mobileQueue() {
        return new Queue(rabbitMqProperties.getMobileQueueName());
    }

    @Bean
    public HeadersExchange createExchange() {
        return new HeadersExchange(rabbitMqProperties.getExchangeName());
    }

    @Bean
    public Binding desktopQueueBinding() {
        return BindingBuilder.bind(desktopQueue())
                .to(createExchange())
                .whereAll(rabbitMqProperties.getHeaderConditions())
                .match();
    }

    @Bean
    public Binding mobileQueueBinding() {
        return BindingBuilder.bind(mobileQueue())
                .to(createExchange())
                .whereAll(rabbitMqProperties.getHeaderConditions())
                .match();
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
