package com.example;

import com.example.randomapi.application.service.RandomBooleanService;
import com.example.randomapi.port.RandomBooleanPort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RandomBooleanApi {

    public static void main(String[] args) {
        SpringApplication.run(RandomBooleanApi.class, args);
    }

    @Bean
    public RandomBooleanPort getRandomBooleanUseCase() {
        return new RandomBooleanService()::getRandomBoolean;
    }
}

