package com.example.journalproject.controller;

import com.example.journalproject.service.JournalEntryService;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.journalproject.entity.JournalEntry;

import java.time.LocalDateTime;
import java.util.HashMap ;
import java.util.Map ; 
import java.util.List ; 
import java.util.ArrayList;
import java.util.Collections ;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    // INITILIZATION
    @Autowired
    private JournalEntryService journalEntryService ;


    //1) GET
    @GetMapping
    public ResponseEntity<?> getAll() {                      // '/journal' : GET
        try {
            List<JournalEntry> journalEntries = journalEntryService.getAll() ;
            return ResponseEntity.ok(Map.of(
                    "success", true ,
                    "message", "Data Fetched Successfully !",
                    "entries", journalEntries
            )) ;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false ,
                            "message" , "Failed to Load Data !" ,
                            "error" , e.getMessage() ,
                            "entries" , Collections.emptyList()
                    )) ;
        }
    }

    //2) GET BASED ON PARAMS 
    @GetMapping("id/{myId}") 
    public ResponseEntity<?> getJournalEntryById(@PathVariable String myId) {
        try {
            JournalEntry entry = journalEntryService.getSpecific(myId) ;
            return ResponseEntity.ok(Map.of(
                    "success", true ,
                    "message" , "Data Fetched Successfully !" ,
                    "entry" , entry
            )) ;
        } catch (Exception e) {
            HttpStatus status = e.getMessage().equals("Journal Not Found !")
                    ? HttpStatus.NOT_FOUND // NOT BEING ASSIGNED ?
                     : HttpStatus.INTERNAL_SERVER_ERROR ;
            return ResponseEntity.status(status).body(Map.of(
                "success", false ,
                "message" , "Failed to Load Journal !" ,
                "error" , e.getMessage() ,
                "entry" , Collections.emptyList() // how to send null value here ?
                    // WLL GIVE NULL POINTER EXCEPTION : SO BEST PRACTICE IS TO REMOVE THIS WHOLE LINE
            ));
        }
    } 


    //3) POST
    @PostMapping                                              // '/journal' : POST
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            myEntry.setDate(LocalDateTime.now());
            JournalEntry success = journalEntryService.saveEntry(myEntry) ;
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success" , true ,
                    "message" , "Journal Entry Created Successfully !",
                    "object" , success
            )); // 201 Created Successfully Do not expecting something in frontend
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success" , false ,
                    "message", "Failed to Create Journal Entry" ,
                    "error" , e.getMessage()
            )); // 500 Error
        }
    }

    //4) DELETE
    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteHJournalEntry(@PathVariable String myId) {
        try {
            JournalEntry entry = journalEntryService.deleteEntry(myId) ;
            return ResponseEntity.ok(Map.of(
                    "success" , true ,
                    "message" , "Journal Entry Deleted Successfully" ,
                    "entry" , entry
            )); // which status should be displayed here
        } catch (Exception e) {
            HttpStatus status = e.getMessage().equals("Journal Not Found !")
                    ? HttpStatus.NOT_FOUND
                     : HttpStatus.INTERNAL_SERVER_ERROR ;
            return ResponseEntity.status(status).body(Map.of(
                "success" , false ,
                "message" , "Failed to Delete Journal Entry",
                "error" , e.getMessage()
            ));
        }
    }

    //5) UPDATE / PUT
    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable String myId, @RequestBody JournalEntry myEntry) {
        try {
             JournalEntry entry = journalEntryService.updateEntry(myId,myEntry) ;
             return ResponseEntity.ok(Map.of(
                 "success" , true ,
                 "message" , "Journal Updated Successfully !" ,
                 "entry" , entry
             ));
        } catch (Exception e) {
            HttpStatus status = e.getMessage().equals("Journal Not Found !")
                         ? HttpStatus.NOT_FOUND
                          : HttpStatus.INTERNAL_SERVER_ERROR ;
            return ResponseEntity.status(status).body(Map.of(
                    "success", false ,
                    "message" , "Failed to Update Journal Entry" ,
                    "error" , e.getMessage()
            ));
        }
    }

     // 6) UPDATE / PATCH
     @PatchMapping("id/{myId}")
     public ResponseEntity<?> patchJournalEntry(@PathVariable String myId, @RequestBody JournalEntry myEntry) {
         try {
             JournalEntry entry = journalEntryService.updateEntry(myId,myEntry) ;
             return ResponseEntity.ok(Map.of(
                     "success" , true ,
                     "message" , "Journal Updated Successfully !" ,
                     "entry" , entry
             ));
         } catch (Exception e) {
             HttpStatus status = e.getMessage().equals("Journal Not Found !")
                     ? HttpStatus.NOT_FOUND
                     : HttpStatus.INTERNAL_SERVER_ERROR ;
             return ResponseEntity.status(status).body(Map.of(
                     "success", false ,
                     "message" , "Failed to Update Journal Entry" ,
                     "error" , e.getMessage()
             ));
         }
     }
}