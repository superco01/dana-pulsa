package com.debrief2.pulsa.mobile;

//import id.test.springboottesting.exception.UserRegistrationException;
//import id.test.springboottesting.model.User;
//import id.test.springboottesting.repository.UserRepository;
import com.debrief2.pulsa.mobile.service.SessionUserDetailsService;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionUserDetailsServiceTest {

    @Mock
    private RpcPublisher rpcPublisher;

    @InjectMocks
    private SessionUserDetailsService sessionUserDetailsService;

    @Mock
    private MemberQueueName memberQueueName;

    final String userId = "159";
    final String userEmail = "habibyafi45@gmail.com";
    final String expectedUserData = "{\"code\":200,\"message\":\"success\",\"data\":{\"id\":159,\"name\":\"Habib Yafi Ardi\",\"email\":\"habibyafi45@gmail.com\",\"username\":\"6281373951739\"}}";
    final String userRpcResponse = "{\"id\":159,\"name\":\"Habib Yafi Ardi\",\"email\":\"habibyafi45@gmail.com\",\"username\":\"6281373951739\"}";


    @Test
    void loadUserByUsername() {

        UserDetails userDetails = new User(userId, userEmail, new ArrayList<>());
//        given(sessionUserDetailsService.loadUserByUsername(userId)).willReturn(userDetails);
        given(rpcPublisher.sendMessage(memberQueueName.getGetProfile(), userId)).willReturn(userRpcResponse);


        UserDetails expected = sessionUserDetailsService.loadUserByUsername(userId);

        assertThat(expected).isNotNull();
    }
}
