package com.debrief2.pulsa.mobile.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SessionAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    RequestFilter requestFilter;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String allowedUrl[] = {"/register", "/login/{phone}", "/verifypin-login", "/forgotpin-otp", "/verify-otp", "/otp/{id}"};

        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers(allowedUrl).permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
//                .sessionManagement().maximumSessions(1).and().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                .sessionManagement().sessionFixation().none();
        httpSecurity.logout().clearAuthentication(true).invalidateHttpSession(true).deleteCookies("SESSION").logoutSuccessHandler(logoutSuccessHandler());
        httpSecurity.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
