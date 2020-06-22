package com.debrief2.pulsa.mobile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestVoucher {
//    @NotBlank(message = "user id must not be null")
    private long userId;
    @NotBlank(message = "page must not be null")
    private int page;
}
