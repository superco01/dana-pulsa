package com.debrief2.pulsa.mobile.utils.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class Register implements Strategy {

    @Override
    public ResponseEntity<?> wrap(HttpSession session,
                                  JsonNode jsonNode,
                                  int code,
                                  String message) {
        System.out.println("Register");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        session.setAttribute("userId", jsonNode.get("id"));
        objectNode.put("code", 201).put("message", "created").set("data", jsonNode);
        return new ResponseEntity<>(objectNode, HttpStatus.CREATED);
    }

    @Override
    public String getStrategyName() {
        return "register";
    }
}
