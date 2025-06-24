package com.example.journalproject.service;

import com.example.journalproject.entity.JournalEntry;
import com.example.journalproject.repository.JournalEntryRepository;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.zip.DataFormatException;
import org.springframework.dao.DataAccessException;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository ;

    //1) GET : Get all entries
    public List<JournalEntry> getAll() {
        try {
            return journalEntryRepository.findAll();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to fetch journal entries from database", e);
        }
    }

    //2) GET ALL/SINGLE : based on params
    public JournalEntry getSpecific(String myId) {
        // a) return journalEntryRepository.findAllById(id) ;
        // b) return single entry :-
        try {
            // return journalEntryRepository.findById(myId).orElse(null);
            return journalEntryRepository.findById(myId).orElseThrow(() -> new RuntimeException("Journal Not Found !")) ;
        }
        catch (Exception e) {
            if (e.getMessage().equals("Journal Not Found !")) {
                throw e;
            }
            throw new RuntimeException("Failed to fetch journal entry from database", e);
        }
    }

    //3) POST : Add entry
    public JournalEntry saveEntry(JournalEntry journalEntry) {
        try {
            return journalEntryRepository.save(journalEntry) ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add Entry !",e) ;
        }
    }

    //4) DELETE : Delete entry
    public JournalEntry deleteEntry(String myId) { // Will return the object that is deleted
        try {
            JournalEntry entry = journalEntryRepository.findById(myId).orElseThrow(()-> new RuntimeException("Journal Not Found !")) ;
            journalEntryRepository.deleteById(myId); // will return true or false
            return entry ;
        } catch (Exception e) {
            if (e.getMessage().equals("Journal Not Found !")) {
                throw e;
            }
            throw new RuntimeException("Failed to Delete Journal Entry !", e);
        }
    }

    // 5) PUT/PATCH : Update entry
    public JournalEntry updateEntry(String myId, JournalEntry object) {
        try {
            JournalEntry existingEntry = journalEntryRepository.findById(myId).orElseThrow(() -> new RuntimeException("Journal Not Found !")) ;

            // ONLY update no-null fields from updateEntry
            if(object.getTitle() != null) {
                existingEntry.setTitle(object.getTitle());
            }
            if(object.getContent() != null) {
                existingEntry.setContent(object.getContent());
            }
            if(object.getDate() != null) {
                existingEntry.setDate(object.getDate()); ;
            }

            return journalEntryRepository.save(existingEntry) ;
        }
        catch(Exception e) {
            if(e.getMessage().equals("Journal Not Found !")) {
                throw e ;
            }
            throw new RuntimeException("Failed to update Data !") ;
        }
    }

}

// @Autowired : DEPENDENCY INJECTION - IN THIS CLASS - IN THIS SERVICE