package com.debrief2.pulsa.mobile.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private String INCORRECT_REQUEST = "INCORRECT_REQUEST";
    private String BAD_REQUEST = "BAD_REQUEST";


    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", new Date());
        body.put("code", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("message", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(InvalidFormatException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadableException(InvalidFormatException ex,
                                                                           HttpHeaders headers,
                                                                           HttpStatus status, WebRequest request) {
        System.out.println("ERROR CONTROLLER ADVICE");
        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", new Date());
        body.put("code", status.value());

        //Get all errors
//        List<String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(x -> x.getDefaultMessage())
//                .collect(Collectors.toList());

        body.put("message", "errors");

        return new ResponseEntity<>(body, headers, status);
    }

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex, WebRequest request) {
//        List<String> details = new ArrayList<>();
//        details.add(ex.getLocalizedMessage());
//        ErrorResponse error = new ErrorResponse(INCORRECT_REQUEST, details);
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }

//    @ExceptionHandler(RecordNotFoundException.class)
//    public final ResponseEntity<ErrorResponse> handleUserNotFoundException
//            (RecordNotFoundException ex, WebRequest request) {
//        List<String> details = new ArrayList<>();
//        details.add(ex.getLocalizedMessage());
//        ErrorResponse error = new ErrorResponse(INCORRECT_REQUEST, details);
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(MissingHeaderInfoException.class)
//    public final ResponseEntity<ErrorResponse> handleInvalidTraceIdException
//            (MissingHeaderInfoException ex, WebRequest request) {
//        List<String> details = new ArrayList<>();
//        details.add(ex.getLocalizedMessage());
//        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
}

