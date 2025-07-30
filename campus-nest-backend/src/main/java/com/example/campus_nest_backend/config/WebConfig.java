package com.example.campus_nest_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 1. CORS for frontend clients (React, mobile, etc.)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173/") // Use specific domains in production
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

//    // 2. Static resources (images, js, css if any)
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/");
//    }

//    // 3. View controllers (optional: for simple Thymeleaf routes)
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        // Example:
//        // registry.addViewController("/login").setViewName("login");
//    }

    // 4. Interceptors (optional: for logging, auth, etc.)
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(new YourCustomInterceptor());
    }

    // 5. Message converters (for customizing Jackson, etc.)
    // You usually don’t need to override this unless customizing JSON mapping.
    // Spring Boot configures it well by default.


    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(5000); // 5 seconds
    }

}
