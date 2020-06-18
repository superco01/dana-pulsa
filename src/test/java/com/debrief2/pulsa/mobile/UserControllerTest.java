package com.debrief2.pulsa.mobile;

import com.debrief2.pulsa.mobile.model.User;
import com.debrief2.pulsa.mobile.utils.rpc.RpcPublisher;
import com.debrief2.pulsa.mobile.utils.rpc.queuename.MemberQueueName;
import com.fasterxml.jackson.databind.ObjectMapper;
//import id.test.springboottesting.model.User;
//import id.test.springboottesting.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
//import org.zalando.problem.ProblemModule;
//import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;


/***
 * Project Name     : spring-boot-testing
 * Username         : Teten Nugraha
 * Date Time        : 12/18/2019
 * Telegram         : @tennugraha
 */

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberQueueName memberQueueName;

    @MockBean
    private RpcPublisher rpcPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    private List<User> userList;

    @BeforeEach
    void setUp() {
        this.userList = new ArrayList<>();
        this.userList.add(new User(1L, "user1@gmail.com", "pwd1","User1"));
        this.userList.add(new User(2L, "user2@gmail.com", "pwd2","User2"));
        this.userList.add(new User(3L, "user3@gmail.com", "pwd3","User3"));

//        objectMapper.registerModule(new ProblemModule());
//        objectMapper.registerModule(new ConstraintViolationProblemModule());
    }

//    @Test
//    void shouldLoginSuccess() throws Exception {
//
//        given(rpcPublisher.sendMessage("login", "081373951739")).willReturn(String.valueOf(userList.get(1)));
//
//        this.mockMvc.perform(get("/api/login/{phone}", "081373951739"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email", is(user.getEmail())))
//                .andExpect(jsonPath("$.name", is(user.getName())));
//    }

    @Test
    void shouldFetchOneUserById() throws Exception {
        final Long userId = 1L;
        final User user = new User(1L, "ten@mail.com","teten","teten");

        given(rpcPublisher.sendMessage("login", "081373951739")).willReturn(String.valueOf(Optional.of(user)));

        this.mockMvc.perform(get("/api/users/{id}", "081373951739"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

//    @Test
//    void shouldReturn404WhenFindUserById() throws Exception {
//        final Long userId = 1L;
//        given(userService.findUserById(userId)).willReturn(Optional.empty());
//
//        this.mockMvc.perform(get("/api/user/{id}", userId))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void shouldCreateNewUser() throws Exception {
//        given(userService.createUser(any(User.class))).willAnswer((invocation) -> invocation.getArgument(0));
//
//        User user = new User(null, "newuser1@gmail.com", "pwd", "Name");
//
//        this.mockMvc.perform(post("/api/users")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.email", is(user.getEmail())))
//                .andExpect(jsonPath("$.password", is(user.getPassword())))
//                .andExpect(jsonPath("$.name", is(user.getName())))
//        ;
//    }
//
//    @Test
//    void shouldReturn400WhenCreateNewUserWithoutEmail() throws Exception {
//        User user = new User(null, null, "pwd", "Name");
//
//        this.mockMvc.perform(post("/api/users")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string("Content-Type", is("application/problem+json")))
//                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
//                .andExpect(jsonPath("$.title", is("Constraint Violation")))
//                .andExpect(jsonPath("$.status", is(400)))
//                .andExpect(jsonPath("$.violations", hasSize(1)))
//                .andExpect(jsonPath("$.violations[0].field", is("email")))
//                .andExpect(jsonPath("$.violations[0].message", is("Email should not be empty")))
//                .andReturn()
//        ;
//    }
//
//    @Test
//    void shouldUpdateUser() throws Exception {
//        Long userId = 1L;
//        User user = new User(userId, "user1@gmail.com", "pwd", "Name");
//        given(userService.findUserById(userId)).willReturn(Optional.of(user));
//        given(userService.updateUser(any(User.class))).willAnswer((invocation) -> invocation.getArgument(0));
//
//        this.mockMvc.perform(put("/api/users/{id}", user.getId())
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email", is(user.getEmail())))
//                .andExpect(jsonPath("$.password", is(user.getPassword())))
//                .andExpect(jsonPath("$.name", is(user.getName())));
//
//    }
//
//    @Test
//    void shouldReturn404WhenUpdatingNonExistingUser() throws Exception {
//        Long userId = 1L;
//        given(userService.findUserById(userId)).willReturn(Optional.empty());
//        User user = new User(userId, "user1@gmail.com", "pwd", "Name");
//
//        this.mockMvc.perform(put("/api/users/{id}", userId)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    void shouldDeleteUser() throws Exception {
//        Long userId = 1L;
//        User user = new User(userId, "user1@gmail.com", "pwd", "Name");
//        given(userService.findUserById(userId)).willReturn(Optional.of(user));
//        doNothing().when(userService).deleteUserById(user.getId());
//
//        this.mockMvc.perform(delete("/api/users/{id}", user.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email", is(user.getEmail())))
//                .andExpect(jsonPath("$.password", is(user.getPassword())))
//                .andExpect(jsonPath("$.name", is(user.getName())));
//
//    }
//
//    @Test
//    void shouldReturn404WhenDeletingNonExistingUser() throws Exception {
//        Long userId = 1L;
//        given(userService.findUserById(userId)).willReturn(Optional.empty());
//
//        this.mockMvc.perform(delete("/api/users/{id}", userId))
//                .andExpect(status().isNotFound());
//
//    }

}
