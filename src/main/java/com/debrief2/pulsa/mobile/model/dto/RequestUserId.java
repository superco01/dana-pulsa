package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestUserId {
    @NotNull(message = "user id must not be null")
    private String id;
}
