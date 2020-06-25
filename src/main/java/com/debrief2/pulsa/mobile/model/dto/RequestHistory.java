package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestHistory {
    @NotBlank(message = "user id must not be null")
    private String userId;
    @NotBlank(message = "page cannot be null")
    private String page;
}
