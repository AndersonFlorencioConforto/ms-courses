package com.ead.course.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SubscriptionDTO {

    @NotNull(message = "Valor do userId não pode ser nulo")
    private UUID userId;
}
