package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RequestVoucherRecommendation {
    private long userId;
    @NotNull(message = "transaction ID must not be null")
    private int transactionId;
}
