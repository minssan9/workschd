package com.voyagerss.api.component.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RereadableFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    )
            throws ServletException, IOException {
        // Skip processing for static resources
        String path = request.getRequestURI();
        if (shouldSkipProcessing(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            ReadableRequestBodyWrapper wrapper = new ReadableRequestBodyWrapper(request);
            wrapper.setAttribute("requestBody", wrapper.getRequestBody());
            filterChain.doFilter(wrapper, response);
        } catch (Exception e) {
            log.error("Error in RereadableFilter: ", e);
            filterChain.doFilter(request, response);
        }
    }

    private boolean shouldSkipProcessing(String path) {
        return path.contains("/static/") || 
               path.contains("/assets/") ||
               path.contains(".ico") ||
               path.contains(".js") ||
               path.contains(".css") ||
               path.contains(".png") ||
               path.contains(".jpg") ||
               path.contains(".gif");
    }
}
