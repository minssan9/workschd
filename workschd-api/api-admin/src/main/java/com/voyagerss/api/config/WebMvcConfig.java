package com.voyagerss.api.config;

import com.voyagerss.api.component.Interceptor.LogInterceptor;
import com.voyagerss.api.component.properties.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@RequiredArgsConstructor
class WebMvcConfig implements WebMvcConfigurer {
    private final LogInterceptor logInterceptor;
    private final AuthProperties authProperties;

 

    @Profile("release")
    @Override
    @Order(1)
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/swagger*")
                .addPathPatterns("/**/*")
                .excludePathPatterns("/board"); // 해당 경로는 인터셉터가 가로채지 않는다.
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(authProperties.getCors().getAllowedOrigins().split(","))
                .allowedMethods(authProperties.getCors().getAllowedMethods().split(","))
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }
}
