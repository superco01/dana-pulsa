package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestVerifyPinLogin {
    @NotNull(message = "id cannot be null")
    private long id;
    @NotNull(message = "pin cannot be null")
    private String pin;
}
