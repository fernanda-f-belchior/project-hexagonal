package com.example.userapi.adapter.out.validator;


import com.example.userapi.port.ValidatorPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ValidatorAdapter implements ValidatorPort {

    @Value("${external-api.registration-url}")
    private String registrationUrl;

    private final RestTemplate restTemplate;

    public ValidatorAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isRegistrationAllowed() {
        return restTemplate.getForObject(this.registrationUrl, Boolean.class);
    }

}
