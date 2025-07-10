package com.example.journalproject.repository;

import com.example.journalproject.entity.ConfigJournalAppEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, String>  {
    // IT TAKES ONLY 2 PARAMETERS: ENTITY(Schema) AND ID(id primary key) !
}
// controller -> (will call) -> Service -> (will call) -> Repository