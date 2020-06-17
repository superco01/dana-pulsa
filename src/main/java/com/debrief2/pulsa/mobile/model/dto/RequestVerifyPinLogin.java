package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RequestVerifyPinLogin {
    @NotBlank(message = "id must not be null")
//    @NotEmpty(message = "empty")
    private String id;
    @NotBlank(message = "pin must not be null")
    private String pin;
}
