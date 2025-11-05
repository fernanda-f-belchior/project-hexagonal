package com.example.randomapi;

import com.example.randomapi.application.service.RandomBooleanService;
import com.example.randomapi.port.RandomBooleanPort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RandomBooleanApplication {

    public static void main(String[] args) {
        SpringApplication.run(RandomBooleanApplication.class, args);
    }

    @Bean
    public RandomBooleanPort getRandomBooleanUseCase() {
        return new RandomBooleanService()::getRandomBoolean;
    }
}

