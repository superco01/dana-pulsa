package com.debrief2.pulsa.mobile.config.auth;

import com.debrief2.pulsa.mobile.service.SessionUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

        //handle multiple login from one source
        if (session.getAttribute("userId") != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            if (!String.valueOf(session.getAttribute("userId")).equals(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()))) {
                SecurityContextHolder.clearContext();
            }
        }

        //validate and set authentication on security context
        if (session.getAttribute("userId") != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.sessionUserDetailsService.loadUserByUsername(String.valueOf(session.getAttribute("userId")));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
