package com.example.journalproject.controller;

import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.service.UserDetailsServiceImpl;
import com.example.journalproject.service.UserEntryService;
import com.example.journalproject.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserEntryService userEntryService ;

    // AUTHENTICATION -------------------------------------------------------
    @Autowired
    // FOR JWT TOKEN AUTHENTICATION : IN-BUILT
    private AuthenticationManager authenticationManager ;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl ;

    @Autowired
    private JwtUtil jwtUtil ;
    // -----------------------------------------------------------------------




    @GetMapping("/health-check")
    public String healthCheck() { // this function is going to get mapped with this "/health-check" route
        return "Ok" ; 
    }

    //3) POST : CREATE USER
    @PostMapping("/signup")
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

    //4) POST MAPPING FOR LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntry userEntry) {
        try {
            Authentication authenticated = authenticationManager.authenticate( // ----------- (1)
                                            new UsernamePasswordAuthenticationToken(
                                                    userEntry.getUsername() ,
                                                    userEntry.getPassword()
                                            ))  ;
            UserDetails userDetails =  userDetailsServiceImpl.loadUserByUsername(userEntry.getUsername()) ;
            String jwt= jwtUtil.generateToken(userDetails.getUsername()) ;

            if(!authenticated.isAuthenticated()) {
                log.error("Error occurred while logging !");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                            "success" , false ,
                        "message" , "Not Authorized !"
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "success" , true ,
                    "message" , "User Login Successful !" ,
                    "token" , jwt
            ));
        } catch (Exception e) {
            log.error("Error occurred while logging !", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success" , false ,
                    "message" , "Internal Server Error"
            ));
        }
    }

}


// 1) Through this one line we are checking that user exists or not (from spring security, UserDetailService and UserDetailServiceImpl)
//    and with the help of Password-encoder in Spring Security we are also check the password !