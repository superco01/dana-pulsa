package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class RequestVoucher {
//    @NotBlank(message = "user id must not be null")
    private long userId;
    @NotBlank(message = "page must not be null")
    private int page;
}
