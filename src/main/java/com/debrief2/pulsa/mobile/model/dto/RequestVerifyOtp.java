package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestVerifyOtp {
    @NotNull(message = "user ID must not be null")
    private long id;
    @NotNull(message = "otp code must not be null")
    private String code;
}
