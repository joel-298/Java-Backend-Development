package com.example.journalproject.controller;

import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.service.JournalEntryService;
import com.example.journalproject.service.UserEntryService;
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
    @Autowired
    private UserEntryService userEntryService ;

    //1) GET
    @GetMapping("/{username}")
    public ResponseEntity<?> getAll(@PathVariable String username) {                      // '/journal' : GET
        try {
            UserEntry user = userEntryService.findByUsername(username) ;
            if(user != null) {
                // List<JournalEntry> journalEntries = journalEntryService.getAll() ;  // Will return all the values in Journal table entries !
                List<JournalEntry> journalEntries = user.getJournalEntries() ; // (2) array from above received object !
                return ResponseEntity.ok(Map.of(
                        "success", true ,
                        "message", "Data Fetched Successfully !",
                        "entries", journalEntries
                )) ;
            } else { // user not found
                 throw new RuntimeException("User Not found !") ;
            }
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
    @GetMapping("id/{username}/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String myId, @PathVariable String username) {
        try {
            UserEntry user = userEntryService.findByUsername(username) ;
            if(user != null) {
                JournalEntry entry = user.getJournalEntries().stream().filter(j->j.getId().equals(myId))
                        .findFirst().orElseThrow(()->new RuntimeException("Journal Not Found !"));
                return ResponseEntity.ok(Map.of(
                        "success", true ,
                        "message" , "Data Fetched Successfully !" ,
                        "entry" , entry
                )) ;
            } else {
                throw new RuntimeException("User Not Found !");
            }
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
    @PostMapping("/{username}")                                              // '/journal' : POST
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String username) {
        try {
            myEntry.setDate(LocalDateTime.now());
            UserEntry userObject = userEntryService.findByUsername(username) ;
            if(userObject != null) {
                JournalEntry success = journalEntryService.saveEntry(myEntry) ;
                userObject.getJournalEntries().add(success) ; // ----------------(1)
                userEntryService.saveEntry(userObject) ;
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "success" , true ,
                        "message" , "Journal Entry Created Successfully !",
                        "object" , success
                )); // 201 Created Successfully Do not expecting something in frontend
            } else { // user not found !
                throw new RuntimeException("User Not Found Entry Failed !") ;
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success" , false ,
                    "message", "Failed to Create Journal Entry" ,
                    "error" , e.getMessage()
            )); // 500 Error
        }
    }

    //4) DELETE
    @DeleteMapping("id/{username}/{myId}")
    public ResponseEntity<?> deleteHJournalEntry(@PathVariable String myId, @PathVariable String username) {
        try {
            UserEntry user = userEntryService.findByUsername(username) ;
            if(user != null) {
                // DELETE FROM USER'S ARRAY
                boolean x = user.getJournalEntries().removeIf(i -> i.getId().equals(myId)) ;
                if(x) {
                    userEntryService.saveEntry(user) ;
                    // DELETE THE JOURNAL ITSELF
                    JournalEntry entry = journalEntryService.deleteEntry(myId) ; // (3)
                    return ResponseEntity.ok(Map.of(
                            "success" , true ,
                            "message" , "Journal Entry Deleted Successfully" ,
                            "entry" , entry
                    )); // which status should be displayed here
                } else {
                    throw new RuntimeException("You are not authorize to perform this function !") ;
                }
            } else {
                throw new RuntimeException("User Not Found !") ;
            }
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
    @PutMapping("id/{username}/{myId}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable String myId, @RequestBody JournalEntry myEntry, @PathVariable String username) {
        try {
             UserEntry user = userEntryService.findByUsername(username) ;
             if(user != null) {
                 boolean x = user.getJournalEntries()
                         .stream().anyMatch(i->i.getId().equals(myId)); // Check if Journal Exists in the same User's array
                 if(x) {
                     JournalEntry entry = journalEntryService.updateEntry(myId,myEntry) ;
                     return ResponseEntity.ok(Map.of(
                             "success" , true ,
                             "message" , "Journal Updated Successfully !" ,
                             "entry" , entry
                     ));
                 } else {
                     throw new RuntimeException("You are Not Authorized to edit this Journal !") ;
                 }
             } else {
                throw new RuntimeException("User Not Found !") ;
             }
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
     @PatchMapping("id/{username}/{myId}")
     public ResponseEntity<?> patchJournalEntry(@PathVariable String myId, @RequestBody JournalEntry myEntry, @PathVariable String username) {
         try {
             UserEntry user = userEntryService.findByUsername(username) ;
             if(user != null) {
                 boolean x = user.getJournalEntries()
                         .stream().anyMatch(i->i.getId().equals(myId)); // Check if Journal Exists in the same User's array
                 if(x) {
                     JournalEntry entry = journalEntryService.updateEntry(myId,myEntry) ;
                     return ResponseEntity.ok(Map.of(
                             "success" , true ,
                             "message" , "Journal Updated Successfully !" ,
                             "entry" , entry
                     ));
                 } else {
                     throw new RuntimeException("You are Not Authorized to edit this Journal !") ;
                 }
             } else {
                throw new RuntimeException("User Not Found !") ;
             }
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



// (1) Here I'm not going to write .add(success.id) but i will write .add(success) ,
// Because in userEntity we have used @DBref which wll automatically make sure that variable contains the id and not the whole object

// (2) In database we are going to see only ids but when this will be fetched it will also show the value of that whole objects ! due to @DBref

// In case of delete we are also going to pass entry instead of entry.id because DBref will automatically obtain id from it.