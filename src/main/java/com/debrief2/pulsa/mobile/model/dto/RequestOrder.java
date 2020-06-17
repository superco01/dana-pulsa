package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestOrder {
//    @NotBlank(message = "user id must not be null")
    private String userId;
    @NotBlank(message = "phone number must not be null")
    private String phoneNumber;
    @NotBlank(message = "catalog ID must not be null")
    private String catalogId;
}
