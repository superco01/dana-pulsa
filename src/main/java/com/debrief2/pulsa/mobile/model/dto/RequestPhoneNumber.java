package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestPhoneNumber {
    @NotBlank(message = "phone number must not be null")
    private String phoneNumber;
}
