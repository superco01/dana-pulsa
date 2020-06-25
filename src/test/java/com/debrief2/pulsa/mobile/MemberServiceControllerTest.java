package com.debrief2.pulsa.mobile;

import com.debrief2.pulsa.mobile.model.dto.*;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class MemberServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberQueueName memberQueueName;

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

        //2xx data
        succesRequestData = "{\"code\":200,\"message\":\"success\"}";
        updatedRequestData = "{\"code\":200,\"message\":\"updated\"}";

        //4xx and 5xx data
        unauthorized = "{\"code\":401,\"message\":\"Unauthorized\"}";
        nameNullData = "{\"code\":400,\"message\":\"name must not be null\"}";

        //session
        mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", userId);
    }

    @Test
    void registerSuccess() throws Exception {
        RequestRegister requestRegister = new RequestRegister("Habib Yafi Ardi", "habibyafi453@gmail.com", "081373951733", "123456");
        String expectedData = "{\"code\":201,\"message\":\"created\",\"data\":{\"id\":1138,\"name\":\"Habib Yafi Ardi\",\"email\":\"habibyafi453@gmail.com\",\"username\":\"6281373951733\"}}";
        String userRegisterRpcResponse = "{\"id\":1138,\"name\":\"Habib Yafi Ardi\",\"email\":\"habibyafi453@gmail.com\",\"username\":\"6281373951733\"}";
        given(rpcPublisher.sendMessage(memberQueueName.getRegister(), objectMapper.writeValueAsString(requestRegister))).willReturn(userRegisterRpcResponse);
        MvcResult result = this.mockMvc.perform(post("/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestRegister))
                .session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedData))
                .andReturn()
        ;
//        Cookie httpSession =  result.getResponse().getCookie("SESSION");
    }

    @Test
    void registerNullRequest() throws Exception {
        RequestRegister requestRegister = new RequestRegister(null, "habibyafi453@gmail.com", "081373951733", "123456");
        given(rpcPublisher.sendMessage(memberQueueName.getRegister(), objectMapper.writeValueAsString(requestRegister))).willReturn("");
        MvcResult result = this.mockMvc.perform(post("/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestRegister))
                .session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(nameNullData))
                .andReturn()
        ;
    }

    @Test
    void loginSuccess() throws Exception {
        given(rpcPublisher.sendMessage(memberQueueName.getLogin(), phone)).willReturn(userRpcResponse);
        this.mockMvc.perform(get("/login/"+phone))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedUserData))
        ;
    }

    @Test
//    @WithMockUser(username = "159")
    void verifyPinSuccess() throws Exception {
        RequestVerifyPinLogin requestVerifyPinLogin = new RequestVerifyPinLogin("159", "123456");
        given(rpcPublisher.sendMessage(memberQueueName.getVerifyPin(), objectMapper.writeValueAsString(requestVerifyPinLogin))).willReturn(userRpcResponse);
        this.mockMvc.perform(post("/verifypin-login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestVerifyPinLogin)))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestData))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void profileSuccess() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", userId);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/profile")
                .session(mockHttpSession);
        given(rpcPublisher.sendMessage(memberQueueName.getGetProfile(), String.valueOf(mockHttpSession.getAttribute("userId")))).willReturn(userRpcResponse);
        this.mockMvc.perform(builder)
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedUserData))
        ;
    }

    @Test
    void unauthorized() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpSession.setAttribute("userId", userId);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/profile")
                .session(mockHttpSession);
        this.mockMvc.perform(builder)
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(unauthorized))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void balanceSuccess() throws Exception {
        given(rpcPublisher.sendMessage(memberQueueName.getGetBalance(), userId)).willReturn(balanceRpcResponse);
        this.mockMvc.perform(get("/balance").session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBalance))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void forgotPinOtpSuccess() throws Exception {
        RequestUserId requestUserId = new RequestUserId(userId);
        given(rpcPublisher.sendMessage(memberQueueName.getSendOTP(), requestUserId.getId())).willReturn(sendOtpRpcResponse);
        this.mockMvc.perform(post("/forgotpin-otp").session(mockHttpSession)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestUserId)))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestData))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void changePinOtpSuccess() throws Exception {
        given(rpcPublisher.sendMessage(memberQueueName.getSendOTP(), userId)).willReturn(sendOtpRpcResponse);
        this.mockMvc.perform(post("/changepin-otp").session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedSendOtpData))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void verifyOtpSuccess() throws Exception {
        RequestVerifyOtp requestVerifyOtp = new RequestVerifyOtp(userId, userPin);
        given(rpcPublisher.sendMessage(memberQueueName.getVerifyOTP(), objectMapper.writeValueAsString(requestVerifyOtp))).willReturn(sendOtpRpcResponse);
        this.mockMvc.perform(post("/verify-otp").session(mockHttpSession)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestVerifyOtp)))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestData))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void changePinSuccess() throws Exception {
        RequestPin requestPin = new RequestPin(Long.valueOf(userId), userPin);
        given(rpcPublisher.sendMessage(memberQueueName.getChangePin(), objectMapper.writeValueAsString(requestPin))).willReturn("updated");
        this.mockMvc.perform(put("/change-pin").session(mockHttpSession)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestPin)))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedRequestData))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void logoutSuccess() throws Exception {
        this.mockMvc.perform(delete("/logout").session(mockHttpSession))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(succesRequestData))
        ;
    }

    @Test
    @WithMockUser(username = "159")
    void getOtpSuccess() throws Exception {
        given(rpcPublisher.sendMessage(memberQueueName.getGetOTP(), userId)).willReturn(sendOtpRpcResponse);
        this.mockMvc.perform(get("/otp/"+userId).session(mockHttpSession)
                .contentType("application/json")
                .content(userId))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedSendOtpData))
        ;
    }
}