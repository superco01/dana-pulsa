package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class RequestVoucherDetails {
    @NotBlank(message = "voucher must not be null")
    private int voucherId;
}
