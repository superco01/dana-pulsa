package com.debrief2.pulsa.mobile.utils;

import com.debrief2.pulsa.mobile.utils.errors.ErrorCodeValue;
import com.debrief2.pulsa.mobile.utils.errors.ErrorList;
import com.debrief2.pulsa.mobile.utils.strategy.Strategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class ResponseWrapper {
    private JsonNode jsonNode;
    private String message = "success";
    private int code = 200;
    private String response;
    private HttpSession session;
    private String strategy;

    @Autowired
    Map<String, Strategy> strategyMap;

//    @Autowired
//    public ResponseWrapper(String response) {
//        this.response = response;
//    }

    public ResponseEntity<?> responseEntity() {
//        session.setAttribute("userId", 123);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        System.out.println("RESPONSE MESSAGE = " + response);
        try {
            jsonNode = objectMapper.readTree(response);
            return strategyMap.get(strategy).wrap(session, jsonNode, code, message);
        } catch (NullPointerException e) {
            objectNode.put("code", code).put("message", message).set("data", jsonNode);
            return new ResponseEntity<>(objectNode, message == "created" ? HttpStatus.CREATED : HttpStatus.OK);
        } catch (JsonProcessingException e) {
            ErrorList errorList = new ErrorList();
            ErrorCodeValue errorCodeValue = errorList.getErrorCodeValueMap().get(response);
            if (errorCodeValue != null) {
                objectNode.put("code", errorCodeValue.getStatus()).put("message", errorCodeValue.getMessage());
                return new ResponseEntity<>(objectNode, errorCodeValue.getHttpStatusCode());
            } else {
                objectNode.put("code", 500).put("message", response);
                return new ResponseEntity<>(objectNode, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } finally {
            strategy = null;
            session = null;
            code = 200;
            message = "success";
        }
    }
}
