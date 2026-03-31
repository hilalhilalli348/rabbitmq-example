package ourcorp.rabbitmqexample.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ourcorp.rabbitmqexample.model.request.UserCreatedRequest;
import ourcorp.rabbitmqexample.publisher.UserCreatedPublisher;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserCreatedPublisher publisher;

    @PostMapping
    public void sendMessage(@RequestBody UserCreatedRequest request) {
        publisher.run(request);
    }


}
