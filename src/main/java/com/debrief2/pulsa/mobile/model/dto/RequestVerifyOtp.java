package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestVerifyOtp {
    @NotBlank(message = "user ID must not be null")
    @Digits(message = "invalid request format", integer = 100, fraction = 0)
    private String id;
    @NotBlank(message = "otp code must not be null")
    private String code;
}
