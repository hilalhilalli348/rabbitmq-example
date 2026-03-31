package ourcorp.rabbitmqexample.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "properties.rabbitmq")
public class RabbitMqProperties {

    private String exchangeName;
    private String firstQueueName;
    private String secondQueueName;

}
