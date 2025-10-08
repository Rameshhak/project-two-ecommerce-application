package com.example.project_two_ecommerce_application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig{

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {

                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins("https://singular-cajeta-a14474.netlify.app/")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                            .allowedHeaders("*")
                            .exposedHeaders("Authorization")
                            .allowCredentials(true);
                }
            };
        }
}



