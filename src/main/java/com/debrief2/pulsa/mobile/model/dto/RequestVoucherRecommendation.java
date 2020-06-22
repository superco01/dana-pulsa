package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestVoucherRecommendation {
    private long userId;
    @NotBlank(message = "transaction ID must not be null")
    private int transactionId;
}
