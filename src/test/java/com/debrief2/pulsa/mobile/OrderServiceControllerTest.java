package com.debrief2.pulsa.mobile;

import com.debrief2.pulsa.mobile.model.dto.*;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.OrderQueueName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class OrderServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderQueueName orderQueueName;

    @MockBean
    private RpcPublisher rpcPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockHttpSession mockHttpSession;

    private final String userId = "159";
    private String phone;
    private String expectedUserData;
    private String expectedBalance;
    private String userRpcResponse;
    private String balanceRpcResponse;
    private String succesRequestData;
    private String expectedSendOtpData;
    private String sendOtpRpcResponse;
    private String userPin;
    private String updatedRequestData;
    private String nameNullData;
    private String unauthorized;
    private String succesRequestDataWithMsg;
    private String sampleData;
    private String phoneCatalog;
    private String createdRequestData;
    private String createdRequestDataWithMsg;
    private String transactionId;
    private String voucherId;
    private String methodId;
    private String deletedRequestDataWithMsg;
    private String serviceUnreachable;

    @BeforeEach
    void setUp() {
        //user data
//        userId = "159";
        userPin = "123456";
        phone = "081373951739";
        expectedUserData = "{\"code\":200,\"message\":\"success\",\"data\":{\"id\":159,\"name\":\"Habib Yafi Ardi\",\"email\":\"habibyafi45@gmail.com\",\"username\":\"6281373951739\"}}";
        userRpcResponse = "{\"id\":159,\"name\":\"Habib Yafi Ardi\",\"email\":\"habibyafi45@gmail.com\",\"username\":\"6281373951739\"}";
        expectedBalance = "{\"code\":200,\"message\":\"success\",\"data\":\"14840000\"}";
        balanceRpcResponse = "\"14840000\"";
        expectedSendOtpData = "{\"code\":200,\"message\":\"success\",\"data\":{\"id\":7,\"userId\":159,\"code\":\"9175\"}}";
        sendOtpRpcResponse = "{\"id\":7,\"userId\":159,\"code\":\"9175\"}";
        sampleData = "{\"dummy\":\"sampleData\"}";
        phoneCatalog = "08520";
        transactionId = "750";
        methodId = "1";
        voucherId = "0";

        //2xx data
        succesRequestData = "{\"code\":200,\"message\":\"success\"}";
        updatedRequestData = "{\"code\":200,\"message\":\"updated\"}";
        createdRequestDataWithMsg = "{\"code\":201,\"message\":\"created\",\"data\":{\"dummy\":\"sampleData\"}}";
        succesRequestDataWithMsg = "{\"code\":200,\"message\":\"success\",\"data\":{\"dummy\":\"sampleData\"}}";
        deletedRequestDataWithMsg = "{\"code\":200,\"message\":\"deleted\",\"data\":{\"dummy\":\"sampleData\"}}";

        //4xx and 5xx data
        unauthorized = "{\"code\":401,\"message\":\"Unauthorized\"}";
        nameNullData = "{\"code\":400,\"message\":\"name must not be null\"}";
        serviceUnreachable = "{\"code\":500,\"message\":\"member service unreachable\"}";

        //session
        mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", userId);
    }

    @Test
    @WithMockUser(username = userId)
    void recentNumberSuccess() throws Exception {
        given(rpcPublisher.sendMessage(orderQueueName.getGetRecentNumber(), userId)).willReturn(sampleData);
        this.mockMvc.perform(get("/recent-number").session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = userId)
    void serviceUnreachable() throws Exception {
        given(rpcPublisher.sendMessage(orderQueueName.getGetRecentNumber(), userId)).willReturn("member service unreachable");
        this.mockMvc.perform(get("/recent-number").session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(serviceUnreachable))
        ;
    }

    @Test
    @WithMockUser(username = userId)
    void catalogSuccess() throws Exception {
        given(rpcPublisher.sendMessage(orderQueueName.getGetAllCatalog(), phoneCatalog)).willReturn(sampleData);
        this.mockMvc.perform(get("/catalog/" + phoneCatalog).session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = userId)
    void orderSuccess() throws Exception {
        RequestOrder requestOrder = new RequestOrder(userId, phone, "20");
        given(rpcPublisher.sendMessage(orderQueueName.getCreateTransaction(), objectMapper.writeValueAsString(requestOrder))).willReturn(sampleData);
        this.mockMvc.perform(post("/order").session(mockHttpSession)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestOrder)))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(createdRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = userId)
    void paySuccess() throws Exception {
        RequestPay requestPay = new RequestPay(userId, transactionId, methodId, voucherId);
        given(rpcPublisher.sendMessage(orderQueueName.getPay(), objectMapper.writeValueAsString(requestPay))).willReturn(sampleData);
        this.mockMvc.perform(post("/pay").session(mockHttpSession)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestPay)))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = userId)
    void transactionInProgressSuccess() throws Exception {
        RequestHistory requestHistory = new RequestHistory(userId, "1");
        given(rpcPublisher.sendMessage(orderQueueName.getGetHistoryInProgress(), objectMapper.writeValueAsString(requestHistory))).willReturn(sampleData);
        this.mockMvc.perform(get("/transaction/in-progress/" + 1).session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = userId)
    void transactionCompletedSuccess() throws Exception {
        RequestHistory requestHistory = new RequestHistory(userId, "1");
        given(rpcPublisher.sendMessage(orderQueueName.getGetHistoryCompleted(), objectMapper.writeValueAsString(requestHistory))).willReturn(sampleData);
        this.mockMvc.perform(get("/transaction/completed/" + 1).session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = userId)
    void transactionDetailsSuccess() throws Exception {
        ObjectNode serviceReq = objectMapper.createObjectNode();
        serviceReq.put("userId", userId).put("transactionId", transactionId);
        given(rpcPublisher.sendMessage(orderQueueName.getGetTransactionByIdByUserId(), objectMapper.writeValueAsString(serviceReq))).willReturn(sampleData);
        this.mockMvc.perform(get("/transaction/details/" + transactionId).session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = userId)
    void transactionCancelSuccess() throws Exception {
        ObjectNode serviceReq = objectMapper.createObjectNode();
        serviceReq.put("userId", userId).put("transactionId", 750);
        given(rpcPublisher.sendMessage(orderQueueName.getCancel(), objectMapper.writeValueAsString(serviceReq))).willReturn(sampleData);
        this.mockMvc.perform(delete("/transaction/cancel/" + transactionId).session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(deletedRequestDataWithMsg))
        ;
    }
}
