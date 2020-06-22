package com.debrief2.pulsa.mobile.controller;

import com.debrief2.pulsa.mobile.model.dto.*;
import com.debrief2.pulsa.mobile.utils.ResponseWrapper;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.OrderQueueName;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController()
public class OrderServiceController {
    @Autowired
    private OrderQueueName orderQueueName;

    @Autowired
    private RpcPublisher rpcPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResponseWrapper responseWrapper;

//    RpcPublisher rpcPublisher;
//
//    {
//        try {
//            rpcPublisher = new RpcPublisher("amqp://jeniyqrm:hHDvRyHfYMdmD48o_oGOzSMgrGK4bTvJ@jellyfish.rmq.cloudamqp.com/jeniyqrm");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @GetMapping(value = "/recent-number")
    public ResponseEntity<?> getRecentNumber(HttpSession httpSession) {
        String response = rpcPublisher.sendMessage(orderQueueName.getGetRecentNumber(), String.valueOf(httpSession.getAttribute("userId")));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/catalog/{phone}")
    public ResponseEntity<?> getCatalog(@PathVariable("phone") @NotNull(message = "phone must not be null") String phone) {
        String response = rpcPublisher.sendMessage(orderQueueName.getGetAllCatalog(), phone);
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @PostMapping(value = "/order")
    public ResponseEntity<?> order(@Valid @RequestBody RequestOrder requestOrder, HttpSession httpSession) {
        requestOrder.setUserId((String.valueOf(httpSession.getAttribute("userId"))));
        String response = rpcPublisher.sendMessage(orderQueueName.getCreateTransaction(), objectMapper.writeValueAsString(requestOrder));
        responseWrapper.setResponse(response);
        responseWrapper.setMessage("created");
        responseWrapper.setCode(201);
        return responseWrapper.responseEntity();    }

    @SneakyThrows
    @PostMapping(value = "/pay")
    public ResponseEntity<?> pay(@Valid @RequestBody RequestPay requestPay, HttpSession httpSession) {
        requestPay.setUserId((String.valueOf(httpSession.getAttribute("userId"))));
        System.out.println("PAY REQUEST " + requestPay);
        String response = rpcPublisher.sendMessage(orderQueueName.getPay(), objectMapper.writeValueAsString(requestPay));
        responseWrapper.setResponse(response);
//        responseWrapper.setCode(202);
        return responseWrapper.responseEntity();    }

    @SneakyThrows
    @GetMapping(value = "/transaction/in-progress/{page}")
    public ResponseEntity<?> getTransactionInProgress(@PathVariable("page") @NotNull(message = "page must not be null") String page, HttpSession httpSession) {
        RequestHistory requestHistory = new RequestHistory((String.valueOf(httpSession.getAttribute("userId"))), page);
        String response = rpcPublisher.sendMessage(orderQueueName.getGetHistoryInProgress(), objectMapper.writeValueAsString(requestHistory));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();    }

    @SneakyThrows
    @GetMapping(value = "/transaction/completed/{page}")
    public ResponseEntity<?> getTransactionCompleted(@PathVariable("page") @NotNull(message = "page must not be null") String page, HttpSession httpSession) {
        RequestHistory requestHistory = new RequestHistory((String.valueOf(httpSession.getAttribute("userId"))), page);
        String response = rpcPublisher.sendMessage(orderQueueName.getGetHistoryCompleted(), objectMapper.writeValueAsString(requestHistory));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @GetMapping(value = "/transaction/details/{id}")
    public ResponseEntity<?> getTransactionDetails(@PathVariable("id") @NotNull(message = "transaction ID must not be null") String id, HttpSession httpSession) {
        ObjectNode serviceReq = objectMapper.createObjectNode();
        serviceReq.put("userId", String.valueOf(httpSession.getAttribute("userId"))).put("transactionId", id);
        String message = objectMapper.writeValueAsString(serviceReq);
        String response = rpcPublisher.sendMessage(orderQueueName.getGetTransactionByIdByUserId(), message);
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @DeleteMapping(value = "/transaction/cancel/{id}")
    public ResponseEntity<?> cancelTransaction(@PathVariable("id") @NotNull(message = "transaction ID must not be null") long id, HttpSession httpSession) {
        ObjectNode serviceReq = objectMapper.createObjectNode();
        serviceReq.put("userId", String.valueOf(httpSession.getAttribute("userId"))).put("transactionId", id);
        String message = objectMapper.writeValueAsString(serviceReq);
        String response = rpcPublisher.sendMessage(orderQueueName.getCancel(), message);
        responseWrapper.setResponse(response);
        responseWrapper.setMessage("deleted");
        return responseWrapper.responseEntity();
    }
}
