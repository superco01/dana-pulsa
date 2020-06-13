package com.debrief2.pulsa.mobile.controller;

import com.debrief2.pulsa.mobile.model.dto.*;
import com.debrief2.pulsa.mobile.utils.ResponseWrapper;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.OrderQueueName;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController()
public class MemberServiceController {
    @Autowired
    private MemberQueueName memberQueueName;

//    @Autowired
//    private RpcPublisher rpcPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    RpcPublisher rpcPublisher;

    {
        try {
            rpcPublisher = new RpcPublisher("amqp://lbfcxugj:eW3yKsOA0FIKKBSzuQz3dVyx5izT0C-8@toad.rmq.cloudamqp.com/lbfcxugj");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RequestRegister requestRegister) {
        String response = rpcPublisher.sendMessage(memberQueueName.getRegister(), objectMapper.writeValueAsString(requestRegister));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        responseWrapper.setMessage("created");
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/login/{phone}")
    public ResponseEntity<?> login(@PathVariable("phone") @NotNull(message = "phone must not be null") String phone) {
        String response = rpcPublisher.sendMessage(memberQueueName.getLogin(), phone);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @PostMapping(value = "verifypin-login")
    public ResponseEntity<?> verifyPinLogin(@Valid @RequestBody RequestVerifyPinLogin requestVerifyPinLogin, HttpSession httpSession) {
        String response = rpcPublisher.sendMessage(memberQueueName.getVerifyPin(), objectMapper.writeValueAsString(requestVerifyPinLogin));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        responseWrapper.setSession(httpSession);
        responseWrapper.setQueue("verifyPin");
//        System.out.println(httpSession.getAttribute("userId"));
//        httpSession.setAttribute("userId", 1);
        return responseWrapper.responseEntity();
//        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<?> getProfile(HttpSession httpSession) {
        String response = rpcPublisher.sendMessage(memberQueueName.getGetProfile(), String.valueOf(httpSession.getAttribute("userId")));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<?> getBalance(HttpSession httpSession) {
        String response = rpcPublisher.sendMessage(memberQueueName.getGetBalance(), String.valueOf(httpSession.getAttribute("userId")));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @PostMapping(value = "/forgotpin-otp")
    public ResponseEntity<?> forgotPinOtp(@Valid @RequestBody RequestUserId requestUserId) {
        String response = rpcPublisher.sendMessage(memberQueueName.getSendOTP(), String.valueOf(requestUserId.getId()));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        responseWrapper.setQueue("forgotpin-otp");
        return responseWrapper.responseEntity();
    }

    @PostMapping(value = "/changepin-otp")
    public ResponseEntity<?> changePinOtp(HttpSession httpSession) {
        String response = rpcPublisher.sendMessage(memberQueueName.getSendOTP(), String.valueOf(httpSession.getAttribute("userId")));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        responseWrapper.setQueue("changepin-otp");
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @PostMapping(value = "/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody RequestVerifyOtp requestVerifyOtp, HttpSession httpSession) {
        String response = rpcPublisher.sendMessage(memberQueueName.getVerifyOTP(), objectMapper.writeValueAsString(requestVerifyOtp));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        responseWrapper.setSession(httpSession);
        responseWrapper.setQueue("verify-otp");
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @PutMapping(value = "/change-pin")
    public ResponseEntity<?> changePin(@Valid @RequestBody RequestPin requestPin, HttpSession httpSession) {
        requestPin.setId(Long.parseLong(String.valueOf(httpSession.getAttribute("userId"))));
        String response = rpcPublisher.sendMessage(memberQueueName.getChangePin(), objectMapper.writeValueAsString(requestPin));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return responseWrapper.responseEntity();
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpSession httpSession) {
        httpSession.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/otp/{id}")
    public ResponseEntity<?> getBalance(@PathVariable("id") @NotNull(message = "ID must not be null") long id) {
        String response = rpcPublisher.sendMessage(memberQueueName.getGetOTP(), String.valueOf(id));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return responseWrapper.responseEntity();
    }
}
