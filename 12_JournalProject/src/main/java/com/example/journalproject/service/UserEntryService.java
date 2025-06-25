package com.example.journalproject.service;


import com.example.journalproject.entity.JournalEntry;
import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.repository.UserEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class UserEntryService {

    @Autowired
    private UserEntryRepository userEntryRepository ;
    @Autowired
    private JournalEntryService journalEntryService ;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;

    //1) GET : Get all users
    public List<UserEntry> getAll() {
        try {
            return userEntryRepository.findAll() ;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to fetch all users from database", e) ;
        }
    }

    //2) GET ALL/SINGLE : based on params
    public UserEntry getSpecific(String myId) {
        try {
            return userEntryRepository.findById(myId).orElseThrow(()-> new RuntimeException("User Not Found !")) ;
        } catch (Exception e) {
            if(e.getMessage().equals("User Not Found !")) {
                log.error(e.getMessage());
                throw e;
            }
            log.error(e.getMessage());
            throw new RuntimeException("Failed to fetch user data from database", e) ;
        }
    }

    //3) POST : CREATE USER
    public UserEntry saveEntry(UserEntry userEntry) {
        try {
            return userEntryRepository.save(userEntry);
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to add Entry !",e) ;
        }
    }
    public UserEntry saveNewEntry(UserEntry userEntry) {
        try {
            userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword())) ;
            userEntry.setRoles(Arrays.asList("USER")); // by default only add one role
            return userEntryRepository.save(userEntry);
        } catch(Exception e) {
            log.error("ERROR : DUPLICATE ENTRY ! " + e.getMessage());
            throw new RuntimeException("Failed to add Entry !",e) ;
        }
    }
    public UserEntry saveAdminEntry(UserEntry userEntry) {
        try {
            userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword())) ;
            userEntry.setRoles(Arrays.asList("USER")); // by default only add one role
            userEntry.setRoles(Arrays.asList("ADMIN"));
            return userEntryRepository.save(userEntry);
        } catch(Exception e) {
            log.error(e.getMessage());
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
                log.error(e.getMessage());
                throw e ;
            }
            log.error(e.getMessage());
            throw new RuntimeException("Failed to delete User !",e) ;
        }
    }
}

// (A) THE BEST PRACTICE TO APPLY TRANSACTIONAL ANNOTATION IS IN SERVICES !

// (B) CIRCULAR DEPENDENCY IN USER ENTRY SERVICE AND JOURNAL ENTRY SERVICE :
//      Remember the example of Manager -> Alice and Bob , Manager creates Desk !
//      Solution : @Lazy Annotation to break this circular dependency !