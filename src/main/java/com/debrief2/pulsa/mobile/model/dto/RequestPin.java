package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPin {
//    @NotBlank(message = "user id must not be null")
    private long id;
    @NotBlank(message = "pin must not be null")
    private String pin;
}
