package com.example.journalproject.repository;

import com.example.journalproject.entity.UserEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEntryRepository extends MongoRepository<UserEntry, String>  {
    // IT TAKES ONLY 2 PARAMETERS: ENTITY(Schema) AND ID(id primary key) !

    // For finding user by its username // CALL THIS METHOD/FUNCTION in SERVICE !
    UserEntry findByUsername(String username) ;
}