package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestOrder {
    private long userId;
    @NotNull(message = "phone number must not be null")
    private String phoneNumber;
    @NotNull(message = "catalog ID must not be null")
    private int catalogId;
}
