package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestVerifyPinLogin {
    @NotBlank(message = "id must not be null")
    private String id;
    @NotBlank(message = "pin must not be null")
    private String pin;
}
