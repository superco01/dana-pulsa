package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class RequestHistory {
    @NotBlank(message = "user id must not be null")
    private String userId;
    @NotBlank(message = "page cannot be null")
    private String page;
}
