package com.example.userapi.adapter.out.validator;


import com.example.userapi.port.ValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ValidatorAdapter implements ValidatorPort {

    @Value("${external-api.registration-url}")
    private String registrationUrl;

    private final RestTemplate restTemplate;

    public ValidatorAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isRegistrationAllowed() {
        log.info("Consultando API Random Boolean para validar rolout");
        Boolean returnedBoolean = restTemplate.getForObject(this.registrationUrl, Boolean.class);
        log.info("Valor retornado pela API Random Boolean: {}", returnedBoolean);
        return returnedBoolean;
    }


}
