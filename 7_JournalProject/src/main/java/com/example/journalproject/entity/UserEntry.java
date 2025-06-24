package com.example.journalproject.entity;


import jakarta.persistence.Id;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "users")
@Data
public class UserEntry {
    // INITILIZATION
    @Id
    private String id ;
    @Indexed(unique = true)
    @NonNull
    private String username ;
    @NonNull
    private String password ;

    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>() ;
}



//@Data discussed in Journal Entry
// DBRef : Creating reference in user's collection/entity of Journal Entries !