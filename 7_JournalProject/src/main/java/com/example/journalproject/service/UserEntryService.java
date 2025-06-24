package com.example.journalproject.service;


import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.repository.UserEntryRepository;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserEntryService {

    @Autowired
    private UserEntryRepository userEntryRepository ;

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
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Username already exists. Please choose a different one") ;
        } catch(Exception e) {
            throw new RuntimeException("Failed to add Entry !",e) ;
        }
    }

    //4) USER BY USERNAME
    public UserEntry findByUsername(String username) {
        return userEntryRepository.findByUsername(username) ;
    }

    //5) DELETE
    public UserEntry deleteEntry(String myId) {
        try {
            UserEntry user = userEntryRepository.findById(myId).orElseThrow(()->new RuntimeException("User Not Found !"));
            userEntryRepository.deleteById(myId);
            return user ;
        } catch (Exception e) {
            if (e.getMessage().equals("User Not Found !")) {
                throw e ;
            }
            throw new RuntimeException("Failed to delete User !",e) ;
        }
    }
}

// @Autowired : DEPENDENCY INJECTION - IN THIS CLASS - IN THIS SERVICE