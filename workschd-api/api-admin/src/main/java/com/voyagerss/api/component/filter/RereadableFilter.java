package com.voyagerss.api.component.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RereadableFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    )
            throws ServletException, IOException {

        ReadableRequestBodyWrapper wrapper = new ReadableRequestBodyWrapper((HttpServletRequest)request);

        try {
            wrapper.setAttribute("requestBody", wrapper.getRequestBody());

            filterChain.doFilter(wrapper, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }
}
