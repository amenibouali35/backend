package com.tunisie.pfe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ Utiliser le même chemin que dans le Controller
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///C:/spring_uploads/documents/");
    }
}