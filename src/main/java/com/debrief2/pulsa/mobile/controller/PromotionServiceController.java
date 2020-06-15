package com.debrief2.pulsa.mobile.controller;

import com.debrief2.pulsa.mobile.model.dto.*;
import com.debrief2.pulsa.mobile.utils.ResponseWrapper;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.OrderQueueName;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.PromotionQueueName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@RestController(value = "/api")
public class PromotionServiceController {
    @Autowired
    private PromotionQueueName promotionQueueName;

    @Autowired
    private RpcPublisher rpcPublisher;

    @Autowired
    private ObjectMapper objectMapper;
//
//    RpcPublisher rpcPublisher;
//
//    {
//        try {
//            rpcPublisher = new RpcPublisher("amqp://tfgupaen:DKiggzp1uqZP7do96IFUFgW-SOPNCDl0@emu.rmq.cloudamqp.com/tfgupaen");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @SneakyThrows
    @GetMapping(value = "/my-vouchers/{page}")
    public ResponseEntity<?> myVoucher(@PathVariable("page") @NotNull(message = "page must not be null") int page, HttpSession httpSession) {
        RequestVoucher requestVoucher = new RequestVoucher(Long.parseLong(String.valueOf(httpSession.getAttribute("userId"))), page);
        String response = rpcPublisher.sendMessage(promotionQueueName.getGetMyVoucher(), objectMapper.writeValueAsString(requestVoucher));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @GetMapping(value = "/vouchers/recommendation/{transactionId}")
    public ResponseEntity<?> voucherRecommendation(@PathVariable("transactionId") @NotNull(message = "transaction ID must not be null") int transactionId, HttpSession httpSession) {
        RequestVoucherRecommendation requestVoucherRecommendation = new RequestVoucherRecommendation(Long.parseLong(String.valueOf(httpSession.getAttribute("userId"))), transactionId);
        requestVoucherRecommendation.setUserId(Long.parseLong(String.valueOf(httpSession.getAttribute("userId"))));
        String response = rpcPublisher.sendMessage(promotionQueueName.getGetVoucherRecommendation(), objectMapper.writeValueAsString(requestVoucherRecommendation));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @GetMapping(value = "/vouchers/promotion/{page}")
    public ResponseEntity<?> voucherPromotion(@PathVariable("page") @NotNull(message = "page must not be null") int page, HttpSession httpSession) {
        RequestVoucher requestVoucher = new RequestVoucher(Long.parseLong(String.valueOf(httpSession.getAttribute("userId"))), page);
        String response = rpcPublisher.sendMessage(promotionQueueName.getGetVoucherPromotion(), objectMapper.writeValueAsString(requestVoucher));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return responseWrapper.responseEntity();
    }

    @SneakyThrows
    @GetMapping(value = "/vouchers/details/{voucherId}")
    public ResponseEntity<?> voucherDetails(@PathVariable("voucherId") @NotNull(message = "voucher ID must not be null") int voucherId) {
        String response = rpcPublisher.sendMessage(promotionQueueName.getGetVoucherDetail(), String.valueOf(voucherId));
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return responseWrapper.responseEntity();
    }
}
