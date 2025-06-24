package com.example.journalproject.service;


import com.example.journalproject.entity.JournalEntry;
import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.repository.UserEntryRepository;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class UserEntryService {

    @Autowired
    private UserEntryRepository userEntryRepository ;
    @Autowired
    private JournalEntryService journalEntryService ;


    //1) GET : Get all users
    public List<UserEntry> getAll() {
        try {
            return userEntryRepository.findAll() ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all users from database", e) ;
        }
    }

    //2) GET ALL/SINGLE : based on params
    public UserEntry getSpecific(String myId) {
        try {
            return userEntryRepository.findById(myId).orElseThrow(()-> new RuntimeException("User Not Found !")) ;
        } catch (Exception e) {
            if(e.getMessage().equals("User Not Found !")) {
                throw e;
            }
            throw new RuntimeException("Failed to fetch user data from database", e) ;
        }
    }

    //3) POST : CREATE USER
    public UserEntry saveEntry(UserEntry userEntry) {
        try {
            return userEntryRepository.save(userEntry);
        } catch(Exception e) {
            throw new RuntimeException("Failed to add Entry !",e) ;
        }
    }

    //4) USER BY USERNAME
    public UserEntry findByUsername(String username) {
        return userEntryRepository.findByUsername(username) ;
    }

    //5) DELETE
    @Transactional
    public void deleteEntry(String myId) {
        try {
            UserEntry user = userEntryRepository.findById(myId).orElseThrow(()->new RuntimeException("User Not Found !"));
            userEntryRepository.deleteById(myId);
            // MANUAL ON DELETE CASCADE
            // Delete from Journal Collections too
            for(JournalEntry entry : user.getJournalEntries()) {
                journalEntryService.deleteEntry(entry.getId()) ;
            }

            return ;
        } catch (Exception e) {
            if (e.getMessage().equals("User Not Found !")) {
                throw e ;
            }
            throw new RuntimeException("Failed to delete User !",e) ;
        }
    }
}

// (A) THE BEST PRACTICE TO APPLY TRANSACTIONAL ANNOTATION IS IN SERVICES !

// (B) CIRCULAR DEPENDENCY IN USER ENTRY SERVICE AND JOURNAL ENTRY SERVICE :
//      Remember the example of Manager -> Alice and Bob , Manager creates Desk !
//      Solution : @Lazy Annotation to break this circular dependency !