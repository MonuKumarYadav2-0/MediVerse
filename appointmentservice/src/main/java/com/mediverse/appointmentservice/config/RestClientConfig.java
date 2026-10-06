package com.mediverse.appointmentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(
            @Value("${doctor.service.url}") String doctorServiceUrl) {

        return RestClient.builder()
                .baseUrl(doctorServiceUrl)
                .build();
    }
}