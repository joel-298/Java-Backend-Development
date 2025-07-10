package com.example.journalproject.controller;
// EXTERNAL API : GET , POST , PUT , PATCH , DELETE

import com.example.journalproject.api.response.WeatherResponse;
import com.example.journalproject.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Profile("dev")
@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService ;



    // 1) GET METHOD
    @GetMapping("/{city}")
    public ResponseEntity<?> getWeather(@PathVariable String city) {
        // Get user's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
        String username = authentication.getName() ;

        try {
            WeatherResponse object =  weatherService.getWeather(city);
            return ResponseEntity.ok(Map.of(
                    "success" , true ,
                    "data" , object ,
                    "message" , "Data fetched Successfully !"
            ));
        } catch (Exception e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR ;
            return ResponseEntity.status(status).body(Map.of(
                    "success", false ,
                    "message" , "Internal Server Error !",
                    "error" , e.getMessage()
            ));
        }
    }



    // 2) POST METHOD
    @PostMapping
    public void postFunction() {
        weatherService.postFunction();;
    }



    // 3) PUT METHOD
    public void putFunction() {
        weatherService.putFunction();
    }
    // 4) PATCH METHOD
    public void patchFunction() {
        weatherService.patchFunction();
    }
    // 5) DELETE METHOD
    public void deleteFunction() {
        weatherService.deleteFunction(); // we can pass id or something in this function
    }
}
