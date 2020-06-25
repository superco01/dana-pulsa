package com.debrief2.pulsa.mobile.controller;

import com.debrief2.pulsa.mobile.model.dto.*;
import com.debrief2.pulsa.mobile.utils.ResponseWrapper;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.PromotionQueueName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@RestController(value = "/api")
public class PromotionServiceController {
    @Autowired
    private PromotionQueueName promotionQueueName;

    @Autowired
    private RpcPublisher rpcPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResponseWrapper responseWrapper;

    @GetMapping(value = "/my-vouchers/{page}")
    public ResponseEntity<?> myVoucher(@PathVariable("page") @NotNull(message = "page must not be null") int page, Principal principal) throws JsonProcessingException {
        RequestVoucher requestVoucher = new RequestVoucher(Long.parseLong(String.valueOf(principal.getName())), page);
        String response = rpcPublisher.sendMessage(promotionQueueName.getGetMyVoucher(), objectMapper.writeValueAsString(requestVoucher));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/vouchers/recommendation/{transactionId}")
    public ResponseEntity<?> voucherRecommendation(@PathVariable("transactionId") @NotNull(message = "transaction ID must not be null") int transactionId, Principal principal) throws JsonProcessingException {
        RequestVoucherRecommendation requestVoucherRecommendation = new RequestVoucherRecommendation(Long.parseLong(String.valueOf(principal.getName())), transactionId);
        String response = rpcPublisher.sendMessage(promotionQueueName.getGetVoucherRecommendation(), objectMapper.writeValueAsString(requestVoucherRecommendation));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/vouchers/promotion/{page}")
    public ResponseEntity<?> voucherPromotion(@PathVariable("page") @NotNull(message = "page must not be null") int page, Principal principal) throws JsonProcessingException {
        RequestVoucher requestVoucher = new RequestVoucher(Long.parseLong(String.valueOf(principal.getName())), page);
        String response = rpcPublisher.sendMessage(promotionQueueName.getGetVoucherPromotion(), objectMapper.writeValueAsString(requestVoucher));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }

    @GetMapping(value = "/vouchers/details/{voucherId}")
    public ResponseEntity<?> voucherDetails(@PathVariable("voucherId") @NotNull(message = "voucher ID must not be null") int voucherId) {
        String response = rpcPublisher.sendMessage(promotionQueueName.getGetVoucherDetail(), String.valueOf(voucherId));
        responseWrapper.setResponse(response);
        return responseWrapper.responseEntity();
    }
}
