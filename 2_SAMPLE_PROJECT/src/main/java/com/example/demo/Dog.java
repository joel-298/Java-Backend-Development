package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class Dog {
    public String fun() {
        return "Something !" ; 
    }
}

// @COMPONENT : created beans i.e create objects in IOC !