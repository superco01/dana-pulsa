package com.debrief2.pulsa.mobile;

import com.debrief2.pulsa.mobile.model.dto.*;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.PromotionQueueName;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class PromotionServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PromotionQueueName promotionQueueName;

    @MockBean
    private RpcPublisher rpcPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockHttpSession mockHttpSession;

    private String userId, phone, expectedUserData, expectedBalance, userRpcResponse;
    private String balanceRpcResponse;
    private String succesRequestData;
    private String expectedSendOtpData;
    private String sendOtpRpcResponse;
    private String userPin;
    private String updatedRequestData;
    private String nameNullData;
    private String unauthorized;
    private String invalidRequestFormat;
    private String sampleData;
    private String createdRequestDataWithMsg;
    private String succesRequestDataWithMsg;
    private String deletedRequestDataWithMsg;

    @BeforeEach
    void setUp() {
        //user data
        userId = "159";
        userPin = "123456";
        phone = "081373951739";
        expectedUserData = "{\"code\":200,\"message\":\"success\",\"data\":{\"id\":159,\"name\":\"Habib Yafi Ardi\",\"email\":\"habibyafi45@gmail.com\",\"username\":\"6281373951739\"}}";
        userRpcResponse = "{\"id\":159,\"name\":\"Habib Yafi Ardi\",\"email\":\"habibyafi45@gmail.com\",\"username\":\"6281373951739\"}";
        expectedBalance = "{\"code\":200,\"message\":\"success\",\"data\":\"14840000\"}";
        balanceRpcResponse = "\"14840000\"";
        expectedSendOtpData = "{\"code\":200,\"message\":\"success\",\"data\":{\"id\":7,\"userId\":159,\"code\":\"9175\"}}";
        sendOtpRpcResponse = "{\"id\":7,\"userId\":159,\"code\":\"9175\"}";
        sampleData = "{\"dummy\":\"sampleData\"}";

        //2xx data
        succesRequestData = "{\"code\":200,\"message\":\"success\"}";
        updatedRequestData = "{\"code\":200,\"message\":\"updated\"}";

        //4xx and 5xx data
        unauthorized = "{\"code\":401,\"message\":\"Unauthorized\"}";
        nameNullData = "{\"code\":400,\"message\":\"name must not be null\"}";
        invalidRequestFormat = "{\"code\":400,\"message\":\"invalid request format\"}";
        succesRequestData = "{\"code\":200,\"message\":\"success\"}";
        updatedRequestData = "{\"code\":200,\"message\":\"updated\"}";
        createdRequestDataWithMsg = "{\"code\":201,\"message\":\"created\",\"data\":{\"dummy\":\"sampleData\"}}";
        succesRequestDataWithMsg = "{\"code\":200,\"message\":\"success\",\"data\":{\"dummy\":\"sampleData\"}}";
        deletedRequestDataWithMsg = "{\"code\":200,\"message\":\"deleted\",\"data\":{\"dummy\":\"sampleData\"}}";

        //session
        mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", userId);
    }
    //finish this first in the morning
    @Test
    @WithMockUser(username = "159")
    void myVoucherSuccess() throws Exception {
        RequestVoucher requestVoucher = new RequestVoucher(Long.parseLong(userId), 1);
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", 159);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/my-vouchers/"+1)
                .session(mockHttpSession);

        given(rpcPublisher.sendMessage(promotionQueueName.getGetMyVoucher(), objectMapper.writeValueAsString(requestVoucher))).willReturn(sampleData);
        this.mockMvc.perform(builder)
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void voucherRecommendationSuccess() throws Exception {
        RequestVoucherRecommendation requestVoucherRecommendation = new RequestVoucherRecommendation(Long.parseLong(String.valueOf(userId)), 830);
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", 159);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/vouchers/recommendation/"+830)
                .session(mockHttpSession);
        given(rpcPublisher.sendMessage(promotionQueueName.getGetVoucherRecommendation(), objectMapper.writeValueAsString(requestVoucherRecommendation))).willReturn(sampleData);
        this.mockMvc.perform(builder)
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void voucherPromotionSuccess() throws Exception {
        RequestVoucher requestVoucher = new RequestVoucher(Long.parseLong(String.valueOf(userId)), 1);
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", 159);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/vouchers/promotion/"+1)
                .session(mockHttpSession);
        given(rpcPublisher.sendMessage(promotionQueueName.getGetVoucherPromotion(), objectMapper.writeValueAsString(requestVoucher))).willReturn(sampleData);
        this.mockMvc.perform(builder)
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void voucherDetailsSuccess() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", 159);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/vouchers/details/"+1)
                .session(mockHttpSession);
        given(rpcPublisher.sendMessage(promotionQueueName.getGetVoucherDetail(), String.valueOf(1))).willReturn(sampleData);
        this.mockMvc.perform(builder)
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestDataWithMsg))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void invalidRequestFormat() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", 159);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/vouchers/details/"+"a")
                .session(mockHttpSession);
        given(rpcPublisher.sendMessage(promotionQueueName.getGetVoucherDetail(), String.valueOf(mockHttpSession.getAttribute("userId")))).willReturn("invalid request format");
        this.mockMvc.perform(builder)
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(invalidRequestFormat))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void myVoucherEmpty() throws Exception {
        String responseExpected = "{\"code\":404,\"message\":\"you don’t have any vouchers\"}";
        String messageRpcResponse = "you don’t have any vouchers";
        RequestVoucher requestVoucher = new RequestVoucher(Long.parseLong(userId), 1);
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", 159);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/my-vouchers/"+1)
                .session(mockHttpSession);

        given(rpcPublisher.sendMessage(promotionQueueName.getGetMyVoucher(), objectMapper.writeValueAsString(requestVoucher))).willReturn(messageRpcResponse);
        this.mockMvc.perform(builder)
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(responseExpected))
        ;
    }
}
