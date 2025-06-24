package com.example.journalproject.controller;

import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserEntryService userEntryService ;

    @GetMapping("/health-check")
    public String healthCheck() { // this function is going to get mapped with this "/health-check" route
        return "Ok" ; 
    }

    //3) POST : CREATE USER
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody UserEntry myEntry) {
        try {
            UserEntry success = userEntryService.saveNewEntry(myEntry);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "User Created Successfully !",
                    "object", success
            )); // 201
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage() ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success","error" ,
                    "message", "Failed to create user Account !",
                    "error", e.getMessage()
            )); // 500 Error
        }
    }

}