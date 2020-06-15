package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RequestRegister {
    @NotNull(message = "name must not be null")
    private String name;
    @NotNull(message = "email must not be null")
    private String email;
    @NotNull(message = "phone must not be null")
    private String phone;
    @NotNull(message = "pin must not be null")
    private String pin;
}
