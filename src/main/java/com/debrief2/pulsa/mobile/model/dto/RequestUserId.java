package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestUserId {
    @NotBlank(message = "user id must not be null")
    @Digits(message = "invalid request format", integer = 100, fraction = 0)
    private String id;
}
