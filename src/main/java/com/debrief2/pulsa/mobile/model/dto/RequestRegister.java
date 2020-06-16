package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RequestRegister {
    @NotBlank(message = "name must not be null")
    private String name;
    @NotBlank(message = "email must not be null")
    private String email;
    @NotBlank(message = "phone must not be null")
    private String phone;
    @NotBlank(message = "pin must not be null")
    private String pin;
}
