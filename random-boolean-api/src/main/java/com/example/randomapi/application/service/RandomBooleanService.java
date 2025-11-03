package com.example.randomapi.application.service;

import java.util.Random;

public class RandomBooleanService {
    private final Random random = new Random();

    public boolean getRandomBoolean(){
        return random.nextBoolean();
    }
}
