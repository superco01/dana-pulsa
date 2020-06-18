package com.debrief2.pulsa.mobile.service;

import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SessionUserDetailsService implements UserDetailsService {
    @Autowired
    private MemberQueueName memberQueueName;

    @Autowired
    private RpcPublisher rpcPublisher;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        String response = rpcPublisher.sendMessage(memberQueueName.getGetProfile(), userId);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        ObjectNode objectNode = (ObjectNode) jsonNode;

        return new User(userId, String.valueOf(objectNode.get("email")), new ArrayList<>());
    }
}
