package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestPhoneCatalog {
    @NotBlank(message = "phone must not be null")
    private String phone;
}
