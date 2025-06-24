package com.edigest.__journalApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class HealthCheck {

    @GetMapping("/health-check")
    public String healthCheck() { // this function is going to get mapped with this "/health-check" route
        return "Ok" ; 
    }
}