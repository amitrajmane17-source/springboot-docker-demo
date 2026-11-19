package com.example.consumer.controller;

import com.example.consumer.service.InfoClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    private final InfoClientService infoClientService;

    public ConsumerController(InfoClientService infoClientService) {
        this.infoClientService = infoClientService;
    }

    @GetMapping("/api/consumer")
    public String getInfoFromOtherService() {
        return infoClientService.getInfo();
    }
}
