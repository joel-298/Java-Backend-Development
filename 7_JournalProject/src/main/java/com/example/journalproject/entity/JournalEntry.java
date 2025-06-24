package com.example.journalproject.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.Date ;


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