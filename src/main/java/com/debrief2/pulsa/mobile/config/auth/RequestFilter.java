package com.debrief2.pulsa.mobile.config.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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
        System.out.println("Filter request "+session.getAttribute("userId"));
//        session.setAttribute("userId", 1);

        if (session.getAttribute("userId") != null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        else if (path.contains("/api/login/") ||
                path.equals("/api/verifypin-login") ||
                path.equals("/api/register")  ||
                path.equals("/api/forgotpin-otp") ||
                path.equals("/api/verify-otp") ||
//                path.equals("/api/change-pin") ||
                path.contains("/api/otp/")) {
//            session.setAttribute("userId", 1);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        else {
            System.out.println("USER CREDENTIALS FAILED");
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }
}
