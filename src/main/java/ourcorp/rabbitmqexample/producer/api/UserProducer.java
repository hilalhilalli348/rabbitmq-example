package ourcorp.rabbitmqexample.producer.api;

import ourcorp.rabbitmqexample.model.request.UserCreatedRequest;

public interface UserProducer {

    void publish(UserCreatedRequest request);

}
