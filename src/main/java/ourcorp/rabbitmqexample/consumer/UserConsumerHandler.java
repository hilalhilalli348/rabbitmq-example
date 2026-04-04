package ourcorp.rabbitmqexample.consumer;

import com.rabbitmq.client.Channel;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ourcorp.rabbitmqexample.model.event.UserCreatedEvent;
import ourcorp.rabbitmqexample.service.api.UserRetryService;
import static ourcorp.rabbitmqexample.util.RetryHeaderUtils.RETRY_HEADER_UTILS;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserConsumerHandler {

    private final UserRetryService userRetryService;

    @SneakyThrows
    @RabbitListener(queues = "#{rabbitMqProperties.mainQueueName}")
    public void handleMessage(@Payload UserCreatedEvent userCreatedEvent,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                              @Headers Map<String, Object> headers,
                              Channel channel) {
        try {
            log.info("Received message [username={}]", userCreatedEvent.getUsername());
            if (userCreatedEvent.getUsername().equals("Hilal")) {
                log.info("Message processed successfully [username={}]", userCreatedEvent.getUsername());
            } else {
                throw new RuntimeException("Simulated processing failure to trigger retry");
            }

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            int retryCount = RETRY_HEADER_UTILS.getRetryCount(headers);
            log.info("Message processing failed [username={}, retryCount={}]", userCreatedEvent.getUsername(),
                    retryCount);
            channel.basicAck(deliveryTag, false);
            userRetryService.handle(userCreatedEvent, headers, retryCount);
        }

    }

}
