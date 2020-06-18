package com.debrief2.pulsa.mobile.utils.strategy;

import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

public interface Strategy {
    ResponseEntity<?> wrap(HttpSession session, JsonNode jsonNode, int code, String message);
    String getStrategyName();
}

