package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrder {
    private String userId;
    @NotBlank(message = "phone number must not be null")
    private String phoneNumber;
    @NotBlank(message = "catalog ID must not be null")
    private String catalogId;
}
