package ourcorp.rabbitmqexample.service.api;

import java.util.Map;
import ourcorp.rabbitmqexample.model.event.UserCreatedEvent;

public interface UserRetryService {

    void handle(UserCreatedEvent userCreatedEvent,
                Map<String, Object> headers,
                int retryCount);

}
