package com.example.journalproject.service;

import com.example.journalproject.entity.JournalEntry;
import com.example.journalproject.entity.UserEntry;
import com.example.journalproject.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository ;
    @Autowired
    @Lazy
    private UserEntryService userEntryService ;


    //1) GET : Get all entries
    public List<JournalEntry> getAll() {
        try {
            return journalEntryRepository.findAll();
        }
        catch (Exception e) {
            log.error("Failed to fetch journal entries from database", e);
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
                log.error("Journal Not Found") ;
                throw e;
            }
            log.error(e.getMessage()) ;
            throw new RuntimeException("Failed to fetch journal entry from database", e);
        }
    }

    //3) POST : Add entry
    @Transactional
    public JournalEntry saveEntry(JournalEntry myEntry, String username) {
        try {
            myEntry.setDate(LocalDateTime.now());
            UserEntry userObject = userEntryService.findByUsername(username) ;
            if(userObject != null) {
                JournalEntry success = journalEntryRepository.save(myEntry) ;
                userObject.getJournalEntries().add(success) ;
                userEntryService.saveEntry(userObject) ;
                return success ;
            } else {
                log.error("User Not Found Entry Failed !") ;
                throw new RuntimeException("User Not Found Entry Failed !");
            }
        } catch (Exception e) {
            if(e.getMessage().equals("User Not Found Entry Failed !")) {
                log.error(e.getMessage()) ;
                throw e ;
            }
            log.error(e.getMessage()) ;
            throw new RuntimeException("Failed to add Entry !") ;
        }
    }

    //4) DELETE : Delete entry
        //(a) Being called from User : on Delete cascade !
    public void deleteEntry(String myId) { // Will return the object that is deleted
        try {
            JournalEntry entry = journalEntryRepository.findById(myId).orElseThrow(()-> new RuntimeException("Journal Not Found !")) ;
            journalEntryRepository.deleteById(myId); // will return true or false
        } catch (Exception e) {
            if (e.getMessage().equals("Journal Not Found !")) {
                log.error(e.getMessage());
                throw e;
            }
            log.error(e.getMessage());
            throw new RuntimeException("Failed to Delete Journal Entry !", e);
        }
    }
        //(b) Being called form Journal : delete form user.journal array to
    @Transactional
    public JournalEntry deleteEntryUser(String myId, String username) {
        try {
            UserEntry user = userEntryService.findByUsername(username) ;
            if(user != null) {
                boolean x = user.getJournalEntries().removeIf(i->i.getId().equals(myId)) ;
                if(x) {
                    userEntryService.saveEntry(user) ;

//                    // Will roll back this entire function if error occurs in between
//                    // === Simulated Runtime Error Here ===
//                    if(true) {
//                        throw new RuntimeException("Simulated Failure After User Update");
//                    }

                    // DELETE THE JOURNAL ITSELF
                    JournalEntry entry = journalEntryRepository.findById(myId).orElseThrow(()-> new RuntimeException("Journal Not Found !")) ;
                    journalEntryRepository.deleteById(myId);
                    return entry ;
                } else {
                    throw new RuntimeException("You are not authorize to perform this function !") ;
                }
            } else {
                throw new RuntimeException("User Not Found !") ;
            }
        } catch (Exception e) {
            if(e.getMessage().equals("Journal Not Found !"))  {
                log.error(e.getMessage());
                throw e ;
            } else if (e.getMessage().equals("User Not Found !")) {
                log.error(e.getMessage());
                throw e ;
            } else if(e.getMessage().equals("You are not authorize to perform this function !")) {
                log.error(e.getMessage());
                throw e ;
            } else {
                log.error(e.getMessage());
                throw new RuntimeException("Failed to Delete Journal Entry !");
            }
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
                log.error(e.getMessage());
                throw e ;
            }
            log.error(e.getMessage());
            throw new RuntimeException("Failed to update Data !") ;
        }
    }

}

// @Autowired : DEPENDENCY INJECTION - IN THIS CLASS - IN THIS SERVICE


// (B) CIRCULAR DEPENDENCY IN USER ENTRY SERVICE AND JOURNAL ENTRY SERVICE :
//      Remember the example of Manager -> Alice and Bob , Manager creates Desk !
//      Solution : @Lazy Annotation to break this circular dependency !