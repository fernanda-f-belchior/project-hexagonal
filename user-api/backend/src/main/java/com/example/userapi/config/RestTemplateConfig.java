package com.example.userapi.config;




import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "RestTemplateConfig")
    public RestTemplate restTemplate() throws Exception {
        // Carrega o truststore
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (InputStream trustStream = new ClassPathResource("truststore.jks").getInputStream()) {
            trustStore.load(trustStream, "changeit".toCharArray());
        }

        // Cria o contexto SSL com o truststore
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(trustStore, null)
                .build();

        // Define o contexto SSL como padrão para conexões HTTPS
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // Usa uma fábrica simples de requisições (sem Apache HttpClient)
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }
}