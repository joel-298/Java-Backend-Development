package com.example.journalproject.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date ;


@Document(collection = "journal_entries")
public class JournalEntry {
    // INITILIZATION
    @Id
    private String id ;
    private String title ; 
    private String content ;
    private LocalDateTime date ;

    // SET VALUES :
    public void setId(String id) {
        this.id = id; 
    }
    public void setTitle(String title) {
        this.title = title ; 
    }
    public void setContent(String content) {
        this.content = content ; 
    }
    public void setDate(LocalDateTime date) { this.date = date; }
    // GET VALUES :
    public String getId() { return id ; }
    public String getTitle() {
        return title ; 
    }
    public String getContent() {
        return content ; 
    }
    public LocalDateTime getDate() { return date; }

}


// @Id for PRIMARY KEY
// @Document to map it with the Schema || table