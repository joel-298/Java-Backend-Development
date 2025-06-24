package com.example.journalproject.controller;

import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserEntryService userEntryService ;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;

    // SEE ALL THE USERS
    @GetMapping("/all_users")
    public ResponseEntity<?> getAllUsers() {
        // Fetch value from spring security
        List<UserEntry> all = userEntryService.getAll();
        if(all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK) ;
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // CREATE MORE ADMINS : PERFORMED BY ADMINS ONLY
    //3) POST : CREATE ADMIN
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody UserEntry myEntry) {
        try {
            UserEntry success = userEntryService.saveAdminEntry(myEntry);
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
