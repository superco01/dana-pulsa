package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestPay {
    private long userId;
    @NotNull(message = "transaction ID must not be null")
    private int transactionId;
    @NotNull(message = "method ID must not be null")
    private int methodId;
    @NotNull(message = "voucher ID must not be null")
    private  int voucherId;
}
