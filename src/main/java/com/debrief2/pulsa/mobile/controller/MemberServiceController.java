package com.debrief2.pulsa.mobile.controller;

import com.debrief2.pulsa.mobile.model.dto.*;
import com.debrief2.pulsa.mobile.utils.ResponseWrapper;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;

@RestController()
public class MemberServiceController {
    @Autowired
    private MemberQueueName memberQueueName;

    @Autowired
    private RpcPublisher rpcPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ResponseWrapper responseWrapper;

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RequestRegister requestRegister, HttpSession httpSession) throws Exception {
        String response = rpcPublisher.sendMessage(memberQueueName.getRegister(), objectMapper.writeValueAsString(requestRegister));
        responseWrapper.setResponse(response);
        responseWrapper.setSession(httpSession);
        responseWrapper.setStrategy(memberQueueName.getRegister());
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/login/{phone}")
    public ResponseEntity<?> login(@PathVariable("phone") @NotNull(message = "phone must not be null") String phone) {
        String response = rpcPublisher.sendMessage(memberQueueName.getLogin(), phone);
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @PostMapping(value = "verifypin-login")
    public ResponseEntity<?> verifyPinLogin(@Valid @RequestBody RequestVerifyPinLogin requestVerifyPinLogin, HttpSession httpSession) throws JsonProcessingException {
        String response = rpcPublisher.sendMessage(memberQueueName.getVerifyPin(), objectMapper.writeValueAsString(requestVerifyPinLogin));
        responseWrapper.setResponse(response);
        responseWrapper.setSession(httpSession);
        responseWrapper.setStrategy(memberQueueName.getVerifyPin());
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        String response = rpcPublisher.sendMessage(memberQueueName.getGetProfile(), String.valueOf(principal.getName()));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<?> getBalance(Principal principal) {
        String response = rpcPublisher.sendMessage(memberQueueName.getGetBalance(), String.valueOf(principal.getName()));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @PostMapping(value = "/forgotpin-otp")
    public ResponseEntity<?> forgotPinOtp(@Valid @RequestBody RequestUserId requestUserId) {
        String response = rpcPublisher.sendMessage(memberQueueName.getSendOTP(), String.valueOf(requestUserId.getId()));
        responseWrapper.setResponse(response);
        responseWrapper.setStrategy(memberQueueName.getSendOTP());
        return responseWrapper.responseEntity();
    }

    @PostMapping(value = "/changepin-otp")
    public ResponseEntity<?> changePinOtp(Principal principal) {
        String response = rpcPublisher.sendMessage(memberQueueName.getSendOTP(), String.valueOf(principal.getName()));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @PostMapping(value = "/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody RequestVerifyOtp requestVerifyOtp, HttpSession httpSession) throws JsonProcessingException {
        String response = rpcPublisher.sendMessage(memberQueueName.getVerifyOTP(), objectMapper.writeValueAsString(requestVerifyOtp));
        responseWrapper.setResponse(response);
        responseWrapper.setSession(httpSession);
        responseWrapper.setStrategy(memberQueueName.getVerifyOTP());
        return responseWrapper.responseEntity();
    }

    @PutMapping(value = "/change-pin")
    public ResponseEntity<?> changePin(@Valid @RequestBody RequestPin requestPin, Principal principal) throws JsonProcessingException {
        requestPin.setId(Long.parseLong(String.valueOf(principal.getName())));
        String response = rpcPublisher.sendMessage(memberQueueName.getChangePin(), objectMapper.writeValueAsString(requestPin));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/otp/{id}")
    public ResponseEntity<?> getOtp(@PathVariable("id") @NotNull(message = "ID must not be null") int id) {
        String response = rpcPublisher.sendMessage(memberQueueName.getGetOTP(), String.valueOf(id));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }
}
