package com.example.journalproject.entity;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "journal_entries")
@Data
public class JournalEntry {
    // INITILIZATION
    @Id
    private String id ;
    @NonNull
    private String title ; 
    private String content ;
    private LocalDateTime date ;



}


// @Id for PRIMARY KEY
// @Document to map it with the Schema || table

// @Data : From a single notation we are going to import
            // @Getter,
            // @Setter,
            // @RequiredArgsConstructor,
            // @ToString,
            // @EqualsAndHashCode,
            // @Value
//@Getter // From Lombok : Reduces the lines of code by automatically setting get value of parameters functions
//@Setter // FROM Lombok : Reduces the lines of code by automatically setting value of parameters functions