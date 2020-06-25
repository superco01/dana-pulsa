package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPay {
    private String userId;
    @NotBlank(message = "transaction ID must not be null")
    private String transactionId;
    @NotBlank(message = "method ID must not be null")
    private String methodId;
    @NotBlank(message = "voucher ID must not be null")
    private String voucherId;
}
