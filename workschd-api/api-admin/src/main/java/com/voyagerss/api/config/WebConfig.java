package com.voyagerss.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
public class WebConfig {
    
    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);
    
    @Bean
    public RestTemplate restTemplate() {
        // Use buffering request factory to allow reading the response multiple times
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        BufferingClientHttpRequestFactory bufferingRequestFactory = new BufferingClientHttpRequestFactory(requestFactory);
        
        RestTemplate restTemplate = new RestTemplate(bufferingRequestFactory);
        
        // Configure Jackson ObjectMapper for better JSON handling
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // Add the configured converter to the RestTemplate
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        
        restTemplate.getMessageConverters().add(0, converter);
        
        // Add logging interceptor
        restTemplate.setInterceptors(Collections.singletonList(new LoggingInterceptor()));
        
        return restTemplate;
    }
    
    @Bean
    public WebClient webClient() {
        // Configure WebClient to log requests and responses
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // 16MB buffer
                .build();
                
        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }
    
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("WebClient Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> 
                values.forEach(value -> log.info("{}={}", name, value))
            );
            return Mono.just(clientRequest);
        });
    }
    
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("WebClient Response Status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
    
    static class LoggingInterceptor implements ClientHttpRequestInterceptor {
        
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            logRequest(request, body);
            ClientHttpResponse response = execution.execute(request, body);
            logResponse(response);
            return response;
        }
        
        private void logRequest(HttpRequest request, byte[] body) {
            log.info("============================= Request Begin ==============================");
            log.info("URI: {}", request.getURI());
            log.info("Method: {}", request.getMethod());
            log.info("Headers: {}", request.getHeaders());
            log.info("Request Body: {}", new String(body, StandardCharsets.UTF_8));
            log.info("============================= Request End ================================");
        }
        
        private void logResponse(ClientHttpResponse response) throws IOException {
            log.info("============================= Response Begin =============================");
            log.info("Status Code: {}", response.getStatusCode());
            log.info("Headers: {}", response.getHeaders());
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
                String responseBody = reader.lines().collect(Collectors.joining("\n"));
                log.info("Response Body: {}", responseBody);
            }
            
            log.info("============================= Response End ===============================");
        }
    }
} 