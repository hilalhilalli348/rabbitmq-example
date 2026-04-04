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

    private String mainExchangeName;
    private String retryExchangeName;
    private String dlqExchangeName;

    private String mainRoutingKeyName;
    private String retryRoutingKeyName;
    private String dlqRoutingKeyName;

    private String mainQueueName;
    private String retryQueueName;
    private String dlqQueueName;

    private int maxRetryCount;
    private int retryTtlMs;

}
