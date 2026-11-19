package com.example.consumer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InfoClientService {

    private final RestTemplate restTemplate;

    @Value("${info.service.url}")
    private String infoServiceUrl;

    public InfoClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getInfo() {
        String url = infoServiceUrl + "/api/info";
        return restTemplate.getForObject(url, String.class);
    }
}
