package com.exemple.projectapi.application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidatorService {

    private final RestTemplate restTemplate;

    public ValidatorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isRegistrationAllowed() {
        String url = "http://localhost:8080/api/random/boolean";
        return restTemplate.getForObject(url, Boolean.class);
    }
}
