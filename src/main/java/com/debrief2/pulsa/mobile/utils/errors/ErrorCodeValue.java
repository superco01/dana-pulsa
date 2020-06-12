package com.debrief2.pulsa.mobile.utils.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorCodeValue {
    private String message;
    private int status;
    private HttpStatus httpStatusCode;
}
