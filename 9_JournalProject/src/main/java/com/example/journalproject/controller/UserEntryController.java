package com.example.journalproject.controller;

import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.service.JournalEntryService;
import com.example.journalproject.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;

//    // 1) GET // ADMIN PROPERTY !
//    @GetMapping
//    public ResponseEntity<?> getAll() {                                    // '/journal' : GET
//        try {
//            List<UserEntry> userEntries = userEntryService.getAll() ;
//            return ResponseEntity.ok(Map.of(
//                    "success", true ,
//                    "message", "Data Fetched Successfully !",
//                    "entry", userEntries
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of(
//                            "success", false ,
//                            "message", "Failed to Load Data !" ,
//                            "error", e.getMessage() ,
//                            "entries" , Collections.emptyList()
//                    )) ;
//        }
//    }

    //2) GET BASED ON PARAMS
    @GetMapping
    public ResponseEntity<?> getJournalEntryById() {
        try {
            // Get Username from the HEADER SPRING SECURITY : because this step will run only after authentication from Spring Security !
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

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


    //4) PUT : UPDATE USER : // NOW FOCUS ON THIS PART ONLY
    @PutMapping
    public ResponseEntity<?> updateEntry(@RequestBody UserEntry myEntry) {
        // Get Username from the HEADER SPRING SECURITY : because this step will run only after authentication from Spring Security !
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntry obj = userEntryService.findByUsername(username) ;
        if(obj != null) {
            obj.setUsername(myEntry.getUsername());
            obj.setPassword(passwordEncoder.encode(myEntry.getPassword())); // Encrypt password before saving it !
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
    @DeleteMapping
    public ResponseEntity<?> deleteEntry() {
        try {
            // Get Username from the HEADER SPRING SECURITY : because this step will run only after authentication from Spring Security !
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            UserEntry user = userEntryService.findByUsername(username) ;
            if(user != null) {
                userEntryService.deleteEntry(user.getId()) ; // pass users id
                // DELETING THE JOURNAL ENTRIES WILL BE DONE IN SERVICES
                // MANUAL ON DELETE CASCADE !

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



// APPLY TRANSACTIONAL AT DELETE USER !