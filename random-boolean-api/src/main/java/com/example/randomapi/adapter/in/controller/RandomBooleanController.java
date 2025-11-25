package com.example.randomapi.adapter.in.controller;

import com.example.randomapi.port.RandomBooleanPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class RandomBooleanController {

    private final RandomBooleanPort randomBooleanPort;

    public RandomBooleanController( RandomBooleanPort randomBooleanPort) {
        this.randomBooleanPort = randomBooleanPort;
    }

    @GetMapping()
    public boolean getRandomBoolean() {
        return randomBooleanPort.getRandomBoolean();
    }

}
