package com.example.java_db_08_lab.configs;

import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {
    @Bean
    public GsonBuilder gsonBuilder() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation();
    }

}
