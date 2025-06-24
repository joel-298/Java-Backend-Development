package com.example.journalproject.controller;

import com.example.journalproject.entity.JournalEntry;
import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.service.JournalEntryService;
import com.example.journalproject.service.UserEntryService;
import com.mongodb.DuplicateKeyException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserEntryController {
    @Autowired
    private UserEntryService userEntryService ;
    @Autowired
    private JournalEntryService journalEntryService ;

    // 1) GET
    @GetMapping
    public ResponseEntity<?> getAll() {                                    // '/journal' : GET
        try {
            List<UserEntry> userEntries = userEntryService.getAll() ;
            return ResponseEntity.ok(Map.of(
                    "success", true ,
                    "message", "Data Fetched Successfully !",
                    "entry", userEntries
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false ,
                            "message", "Failed to Load Data !" ,
                            "error", e.getMessage() ,
                            "entries" , Collections.emptyList()
                    )) ;
        }
    }

    //2) GET BASED ON PARAMS
    @GetMapping("/{username}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String username) {
        try {
            UserEntry entry = userEntryService.findByUsername(username) ;
            if(entry != null) {
                return ResponseEntity.ok(Map.of(
                        "success" , true ,
                        "message","Data Fetched Successfully !",
                        "entry", entry
                ));
            } else {
                throw new RuntimeException("User Not Found !") ;
            }
        } catch (Exception e) {
            HttpStatus status = e.getMessage().equals("User Not Found !")
                                ? HttpStatus.NOT_FOUND
                                : HttpStatus.INTERNAL_SERVER_ERROR ;
            return ResponseEntity.status(status).body(Map.of(
                    "success" , "error" ,
                    "message" , "Failed to Load User !",
                    "error" , e.getMessage() ,
                    "entry", Collections.emptyList()
            ));
        }
    }

    //3) POST : CREATE USER
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody UserEntry myEntry) {
        try {
            UserEntry success = userEntryService.saveEntry(myEntry);
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


    //4) PUT : UPDATE USER
    @PutMapping("/{username}")
    public ResponseEntity<?> updateEntry(@RequestBody UserEntry myEntry, @PathVariable String username) {
        UserEntry obj = userEntryService.findByUsername(username) ;
        if(obj != null) {
            obj.setUsername(myEntry.getUsername());
            obj.setPassword(myEntry.getPassword());
            userEntryService.saveEntry(obj) ;
            return ResponseEntity.ok(Map.of(
                    "success" , "true" ,
                    "message" , "User Updated Successfully !"
            )) ;
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success" , "false" ,
                    "message" , "Error in Updating User !"
            ));
        }
    }

    //5) DELETE : DELETE USER and its entries too from journal_entries
    // MANUAL ON DELETE CASCADE !
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteEntry(@PathVariable String username) {
        try {
            UserEntry user = userEntryService.findByUsername(username) ;
            if(user != null) {
                UserEntry object = userEntryService.deleteEntry(user.getId()) ; // pass users id
                for(JournalEntry entry : object.getJournalEntries()) {
                    journalEntryService.deleteEntry(entry.getId()) ;
                }

                return ResponseEntity.ok(Map.of(
                        "success" , "true" ,
                        "message" , "User and its Journal Entries Deleted Successfully !"
                ));
            } else {
                throw new RuntimeException("User Not Found !") ;
            }
        } catch (Exception e) {
            HttpStatus status = e.getMessage().equals("User Not Found !")
                                ? HttpStatus.NOT_FOUND
                                : HttpStatus.INTERNAL_SERVER_ERROR ;
            return ResponseEntity.status(status).body(Map.of(
                "success" , false ,
                "message" , "Failed To Delete User !" ,
                "error" , e.getMessage()
            ));
        }
    }

}