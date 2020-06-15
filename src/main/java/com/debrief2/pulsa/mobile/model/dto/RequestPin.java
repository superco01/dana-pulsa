package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestPin {
    private long id;
    @NotNull(message = "pin must not be null")
    private String pin;
}
