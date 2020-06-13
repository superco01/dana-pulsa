package com.debrief2.pulsa.mobile.utils;

import com.debrief2.pulsa.mobile.utils.errors.ErrorCodeValue;
import com.debrief2.pulsa.mobile.utils.errors.ErrorList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Data
public class ResponseWrapper {
    private int status;
    private String message = "success";
    private int code = 200;
    private JsonNode data;
    private String response;
    private HttpSession session;
    private String queue = "";
//
//    @Autowired
//    ErrorList errorList;

    public ResponseWrapper(String response) {
        this.response = response;
    }

    public ResponseEntity<?> responseEntity () {
//    public ObjectNode getResult() {
//        ErrorList errorListTest = new ErrorList();
//        Map<String, ErrorCodeValue> errorCodeValuesTest = errorListTest.getErrorCodeValueMap();
//        System.out.println("get from status "+errorCodeValuesTest.get("unknown phone number").getStatus());
//        System.out.println(session.getId());
//        session.setAttribute("userId", 123);
//        System.out.println("get session in wrapper"+session.getAttribute("userId"));
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        System.out.println("RESPONSE MESSAGE = "+response);
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (queue.equals("verifyPin") || queue.equals("verify-otp") && jsonNode.get("email") != null) {
                session.setAttribute("userId", jsonNode.get("id"));
                objectNode.put("code", code).put("message", message);
                return new ResponseEntity<>(objectNode, HttpStatus.OK);
            }
//            else if(queue.equals("changepin-otp")) {
//                ObjectNode data = objectMapper.createObjectNode();
//                data = (ObjectNode) jsonNode;
//                data.remove("createdAt");
//                data.remove("updatedAt");
//                data.remove("code");
//                data.remove("id");
//                objectNode.put("code", code).put("message", message).set("data", jsonNode);
//                return new ResponseEntity<>(objectNode, HttpStatus.OK);
//            }
//            else if(queue.equals("verify-otp")) {
//                session.setAttribute("userId", jsonNode.get("userId"));
//                objectNode.put("code", code).put("message", message);
//                return new ResponseEntity<>(objectNode, HttpStatus.OK);
//            }
            else if (queue.equals("forgotpin-otp")) {
                objectNode.put("code", code).put("message", message);
                return new ResponseEntity<>(objectNode, HttpStatus.OK);
            }
            else {
                objectNode.put("code", code).put("message", message).set("data", jsonNode);
                return new ResponseEntity<>(objectNode, message == "created"?HttpStatus.CREATED:HttpStatus.OK);
            }
        } catch (JsonProcessingException e) {
            System.out.println("Not Success");
            ErrorList errorList = new ErrorList();
            ErrorCodeValue errorCodeValue = errorList.getErrorCodeValueMap().get(response);
            if (errorCodeValue != null) {
                objectNode.put("code", errorCodeValue.getStatus()).put("message", errorCodeValue.getMessage());
                return new ResponseEntity<>(objectNode, errorCodeValue.getHttpStatusCode());
            } else {
                objectNode.put("code", 500).put("message", response);
                return new ResponseEntity<>(objectNode, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
//        return objectNode;
//    }
//
//    public ResponseEntity<?> responseEntity () {
//        return new ResponseEntity<>(objectNode, HttpStatus.OK);
    }
}
