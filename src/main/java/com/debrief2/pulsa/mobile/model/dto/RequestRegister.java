package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
