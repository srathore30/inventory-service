package com.inventory.inventory.Configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedOrigins("https://www.prism-sfa-dev.net", "https://api.prism-sfa-dev.net", "http://localhost:3000","https://dev.prism-sfa-dev.net","https://staging.prism-sfa-dev.net/",
                        "http://staging.prism-sfa-dev.net/",
                        "http://88.222.215.37:9091",
                        "http://88.222.215.37")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*").allowCredentials(true)
                .maxAge(3600);
    }
}
