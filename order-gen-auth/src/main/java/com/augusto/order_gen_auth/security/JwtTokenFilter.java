package com.augusto.order_gen_auth.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenFilter extends GenericFilterBean {
    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
            throws IOException, ServletException {
        var token = tokenProvider.resolveToken((HttpServletRequest) request);
        if (!token.isBlank() && tokenProvider.validateToken(token)) {
            var auth = tokenProvider.getAuth(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        }
        filter.doFilter(request, response);
    }

}
