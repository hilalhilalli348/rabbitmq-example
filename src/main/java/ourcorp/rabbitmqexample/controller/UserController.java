package ourcorp.rabbitmqexample.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ourcorp.rabbitmqexample.model.request.UserCreatedRequest;
import ourcorp.rabbitmqexample.producer.api.UserProducer;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserProducer userProducer;

    @PostMapping
    public void sendMessage(@Valid @RequestBody UserCreatedRequest request) {
        userProducer.publish(request);
    }


}
