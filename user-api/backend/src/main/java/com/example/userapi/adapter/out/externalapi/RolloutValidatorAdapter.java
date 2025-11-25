package com.example.userapi.adapter.out.externalapi;


import com.example.userapi.port.RolloutValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RolloutValidatorAdapter implements RolloutValidatorPort {

    @Value("${external-api.random-boolean-url}")
    private String registrationUrl;

    private final RestTemplate restTemplate;

    public RolloutValidatorAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isRegistrationAllowed() {
        log.info("Consultando API Random Boolean para validar rolout");
        Boolean returnedBoolean = restTemplate.getForObject(this.registrationUrl, Boolean.class);
        log.info("Valor retornado pela API Random Boolean: {}", returnedBoolean);
        return returnedBoolean;

    }

}
