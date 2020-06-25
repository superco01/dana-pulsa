package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestVerifyOtp {
    @NotBlank(message = "user ID must not be null")
    private String id;
    @NotBlank(message = "otp code must not be null")
    private String code;
}
