package com.edigest.__journalApp.entity;

public class JournalEntry {
    // INITILIZATION 
    private long id ; 
    private String title ; 
    private String content ; 

    // SET VALUES : 
    public void setId(long id) {
        this.id = id; 
    }
    public void setTitle(String title) {
        this.title = title ; 
    }
    public void setContent(String content) {
        this.content = content ; 
    }
    
    // GET VALUES : 
    public long getId() {
        return id ; 
    }
    public String getTitle() {
        return title ; 
    }
    public String getContent() {
        return content ; 
    }
 
}
