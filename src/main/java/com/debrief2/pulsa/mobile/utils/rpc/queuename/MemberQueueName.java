package com.debrief2.pulsa.mobile.utils.rpc.queuename;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MemberQueueName {
    private final String register = "register";
    private final String login = "login";
    private final String verifyPin = "verifyPin";
    private final String getProfile = "getProfile";
    private final String getBalance = "getBalance";
    private final String changePin = "changePin";
    private final String sendOTP = "sendOTP";
    private final String verifyOTP = "verifyOTP";
    private final String getOTP = "getOTP";
}
