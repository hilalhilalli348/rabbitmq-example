package ourcorp.rabbitmqexample.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreatedRequest {

    @NotNull
    private Long id;

    @NotEmpty
    private String username;

}
