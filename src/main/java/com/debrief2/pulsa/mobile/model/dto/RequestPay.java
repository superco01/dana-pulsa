package com.debrief2.pulsa.mobile.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestPay {
    @NotBlank(message = "user id must not be null")
    private long userId;
    @NotBlank(message = "transaction ID must not be null")
    private int transactionId;
    @NotBlank(message = "method ID must not be null")
    private int methodId;
    @NotBlank(message = "voucher ID must not be null")
    private  int voucherId;
}
