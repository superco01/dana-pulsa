package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestVerifyOtp {
    @NotBlank(message = "user ID must not be null")
    private String id;
    @NotBlank(message = "otp code must not be null")
    private String code;
}
