package ourcorp.rabbitmqexample.service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ourcorp.rabbitmqexample.config.properties.RabbitMqProperties;
import ourcorp.rabbitmqexample.model.event.UserCreatedEvent;
import ourcorp.rabbitmqexample.service.api.UserRetryService;
import static ourcorp.rabbitmqexample.util.RetryHeaderUtils.RETRY_HEADER_UTILS;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRetryServiceHandler implements UserRetryService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final RabbitMqProperties rabbitMqProperties;

    @Override
    public void handle(UserCreatedEvent userCreatedEvent,
                       Map<String, Object> headers,
                       int retryCount) {
        try {
            if (retryCount < rabbitMqProperties.getMaxRetryCount()) {
                log.info("Sending to retry queue [username={}, retryCount={}]", userCreatedEvent.getUsername(),
                        retryCount + 1);
                sendToRetry(userCreatedEvent, headers);
            } else {
                log.info("Max retry reached, sending to DLQ [username={}, retryCount={}]",
                        userCreatedEvent.getUsername(), retryCount);
                sendToDead(userCreatedEvent, retryCount);
            }
        } catch (Exception e) {
            log.error("Failed to route message to retry/DLQ, message lost [username={}]",
                    userCreatedEvent.getUsername(), e);
        }
    }

    private void sendToRetry(UserCreatedEvent userCreatedEvent,
                             Map<String, Object> headers) {
        MessageProperties props = new MessageProperties();
        props.setContentType(MessageProperties.CONTENT_TYPE_JSON);

        RETRY_HEADER_UTILS.incrementRetryCount(headers).forEach(props::setHeader);

        rabbitTemplate.send(
                rabbitMqProperties.getRetryExchangeName(),
                rabbitMqProperties.getRetryRoutingKeyName(),
                new Message(toBytes(userCreatedEvent), props)
        );
    }

    private void sendToDead(UserCreatedEvent userCreatedEvent, int retryCount) {
        MessageProperties props = new MessageProperties();
        props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        props.setHeader("x-final-retry-count", retryCount);
        props.setHeader("x-failed-at", Instant.now().toString());

        rabbitTemplate.send(
                rabbitMqProperties.getDlqExchangeName(),
                rabbitMqProperties.getDlqRoutingKeyName(),
                new Message(toBytes(userCreatedEvent), props)
        );
    }

    private byte[] toBytes(UserCreatedEvent userCreatedEvent) {
        try {
            return objectMapper.writeValueAsBytes(userCreatedEvent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}