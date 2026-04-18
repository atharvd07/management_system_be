package com.example.ad;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Serve files from the 'uploads' directory
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:H:/1. JAVA Frameworks/React + SpringBoot/ReactSpringbootApplication/backend/management_system_be/uploads");  // Replace with your actual file path
            }
        };
    }
}

