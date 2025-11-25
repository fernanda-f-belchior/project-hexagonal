package com.example.userapi.adapter.out.externalapi;

import com.example.userapi.application.response.PostalCodeAddressResponse;
import com.example.userapi.port.PostalCodeAddressPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class PostalCodeAddressAdapter implements PostalCodeAddressPort {

    @Value("${external-api.postal-code-validation-url}")
    private String postalCodeValidationUrl;
    private final RestTemplate restTemplate;

    public PostalCodeAddressAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PostalCodeAddressResponse fetchAddressByPostalCode(String postalCode) {
        log.info("Consultando API OpenCep para validar e cadastrar endereço");

        String url = postalCodeValidationUrl + postalCode;
        log.info("URL para consulta de endereço: {}", url);

        String response = restTemplate.getForObject(url, String.class);

        PostalCodeAddressResponse postalCodeAddressResponse =  restTemplate.getForObject(url, PostalCodeAddressResponse.class);
        return postalCodeAddressResponse;
    }


}

