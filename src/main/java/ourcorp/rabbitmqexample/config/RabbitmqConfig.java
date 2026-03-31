package ourcorp.rabbitmqexample.config;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitmqConfig {

    public static final String USER_CREATED_QUEUE_NAME = "user-created-queue";
    public static final String USER_CREATED_EXCHANGE_NAME = "user-created-exchange";
    public static final String USER_CREATED_ROUTING_KEY_NAME = "user-created-routing-key";


    @Bean
    public Queue queue() {
        return new Queue(USER_CREATED_QUEUE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(createExchange())
                .with(USER_CREATED_ROUTING_KEY_NAME);
    }

    @Bean
    public DirectExchange createExchange() {
        return new DirectExchange(USER_CREATED_EXCHANGE_NAME);
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
