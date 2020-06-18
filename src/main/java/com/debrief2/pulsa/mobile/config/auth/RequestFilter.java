package com.debrief2.pulsa.mobile.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class RequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final HttpSession session = httpServletRequest.getSession();
        String path = httpServletRequest.getRequestURI();
        System.out.println("Filter Request Path " + path);
        System.out.println("Filter Request " + session.getId());
        System.out.println("Filter Request "+session.getAttribute("userId"));
//        session.setAttribute("userId", 1);

        if (session.getAttribute("userId") != null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        else if (path.contains("/api/login/") ||
                path.equals("/api/verifypin-login") ||
                path.equals("/api/register")  ||
                path.equals("/api/forgotpin-otp") ||
                path.equals("/api/verify-otp") ||
                path.contains("/api/otp/")) {
//            session.setAttribute("userId", 1);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        else {
            System.out.println("USER CREDENTIALS FAILED");
//            throw new InvalidCredentialsException(111111);
//            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("code", 401).put("message", "Unauthorized");
            httpServletResponse.getOutputStream().println(objectMapper.writeValueAsString(objectNode));
        }

//        try {
//            System.out.println("TRY");
//            session.getAttribute("userId");
//            System.out.println(session.getAttribute("userId"));
//            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized with catch");
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//            System.out.println("CATCH");
//            if (path.contains("/api/login/") ||
//                    path.equals("/api/verifypin-login") ||
//                    path.equals("/api/register")  ||
//                    path.equals("/api/forgotpin-otp") ||
//                    path.equals("/api/verify-otp") ||
////                path.equals("/api/change-pin") ||
//                    path.contains("/api/otp/")) {
////            session.setAttribute("userId", 1);
//                filterChain.doFilter(httpServletRequest, httpServletResponse);
//            }
//        } catch (AuthenticationException e) {
//            System.out.println("USER CREDENTIALS FAILED");
//            httpServletResponse.getWriter().write("credentials failed");
////                throw new ResponseStatusException(HttpStatus.OK, "exception on filter", e);
//            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//        }
    }
}
