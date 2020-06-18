package com.debrief2.pulsa.mobile.config.auth;

import com.debrief2.pulsa.mobile.service.SessionUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

    @Autowired
    SessionUserDetailsService sessionUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final HttpSession session = httpServletRequest.getSession();
        String path = httpServletRequest.getRequestURI();
        System.out.println("Filter Request Path     " + path);
        System.out.println("Filter Request Session  " + session.getId());
        System.out.println("Filter Request User ID  " + session.getAttribute("userId"));
//        session.setAttribute("userId", 1);

        if (session.getAttribute("userId") != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.sessionUserDetailsService.loadUserByUsername(String.valueOf(session.getAttribute("userId")));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

//            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
//        else if (path.contains("/api/login/") ||
//                path.equals("/api/verifypin-login") ||
//                path.equals("/api/register")  ||
//                path.equals("/api/forgotpin-otp") ||
//                path.equals("/api/verify-otp") ||
//                path.contains("/api/otp/")) {
////            session.setAttribute("userId", 1);
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//        }
//        else {
//            System.out.println("USER CREDENTIALS FAILED");
//            httpServletResponse.setContentType("application/json");
//            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ObjectNode objectNode = objectMapper.createObjectNode();
//            objectNode.put("code", 401).put("message", "Unauthorized");
//            httpServletResponse.getOutputStream().println(objectMapper.writeValueAsString(objectNode));
//        }

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
