package com.debrief2.pulsa.mobile.controller;

import com.debrief2.pulsa.mobile.model.dto.*;
import com.debrief2.pulsa.mobile.utils.ResponseWrapper;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

//    RpcPublisher rpcPublisher;
//
//    {
//        try {
//            rpcPublisher = new RpcPublisher("amqp://lbfcxugj:eW3yKsOA0FIKKBSzuQz3dVyx5izT0C-8@toad.rmq.cloudamqp.com/lbfcxugj");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RequestRegister requestRegister, HttpSession httpSession) throws Exception{
        System.out.println("Request write as string = "+objectMapper.writeValueAsString(requestRegister));
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

//    @SneakyThrows
    @PostMapping(value = "verifypin-login")
    public ResponseEntity<?> verifyPinLogin(@Valid @RequestBody RequestVerifyPinLogin requestVerifyPinLogin, HttpSession httpSession) throws JsonProcessingException {
        System.out.println(requestVerifyPinLogin);
        String response = rpcPublisher.sendMessage(memberQueueName.getVerifyPin(), objectMapper.writeValueAsString(requestVerifyPinLogin));
        responseWrapper.setResponse(response);
        responseWrapper.setSession(httpSession);
        responseWrapper.setStrategy(memberQueueName.getVerifyPin());
//        System.out.println(httpSession.getAttribute("userId"));
//        httpSession.setAttribute("userId", 1);
         return responseWrapper.responseEntity();
//        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<?> getProfile(HttpSession httpSession, Principal principal) {
        System.out.println("From principal get " + principal.getName());
        String response = rpcPublisher.sendMessage(memberQueueName.getGetProfile(), String.valueOf(httpSession.getAttribute("userId")));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<?> getBalance(HttpSession httpSession) {
        String response = rpcPublisher.sendMessage(memberQueueName.getGetBalance(), String.valueOf(httpSession.getAttribute("userId")));
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
    public ResponseEntity<?> changePinOtp(HttpSession httpSession) {
        String response = rpcPublisher.sendMessage(memberQueueName.getSendOTP(), String.valueOf(httpSession.getAttribute("userId")));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @PostMapping(value = "/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody RequestVerifyOtp requestVerifyOtp, HttpSession httpSession) {
        String response = rpcPublisher.sendMessage(memberQueueName.getVerifyOTP(), objectMapper.writeValueAsString(requestVerifyOtp));
        responseWrapper.setResponse(response);
        responseWrapper.setSession(httpSession);
        responseWrapper.setStrategy(memberQueueName.getVerifyOTP());
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @PutMapping(value = "/change-pin")
    public ResponseEntity<?> changePin(@Valid @RequestBody RequestPin requestPin, HttpSession httpSession) {
        requestPin.setId(Long.parseLong(String.valueOf(httpSession.getAttribute("userId"))));
        String response = rpcPublisher.sendMessage(memberQueueName.getChangePin(), objectMapper.writeValueAsString(requestPin));
        responseWrapper.setResponse(response);
//        responseWrapper.setQueue("change-pin");
        return responseWrapper.responseEntity();
    }

//    @DeleteMapping(value = "/logout")
//    public ResponseEntity<?> logout(HttpSession httpSession, HttpServletRequest httpServletRequest) {
////        httpSession.invalidate();
//        System.out.println("Manual Logout");
//        HttpSession session= httpServletRequest.getSession(false);
//        SecurityContextHolder.clearContext();
//        session= httpServletRequest.getSession(false);
//        if(session != null) {
//            session.invalidate();
//        }
//        for(Cookie cookie : httpServletRequest.getCookies()) {
//            cookie.setMaxAge(0);
//        }
//        return new ResponseEntity<>(objectMapper.createObjectNode().put("code", 200).put("message", "success"), HttpStatus.OK);
//    }

    @GetMapping(value = "/otp/{id}")
    public ResponseEntity<?> getBalance(@PathVariable("id") @NotNull(message = "ID must not be null") int id) {
        String response = rpcPublisher.sendMessage(memberQueueName.getGetOTP(), String.valueOf(id));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }
}
