package com.example.journalproject.cache;


import com.example.journalproject.entity.ConfigJournalAppEntity;
import com.example.journalproject.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository ;

    // Will act as In-Memory Cache : And we will obtain the weather API from this !
    public Map<String,String> APP_CACHE = new HashMap<>();


    @PostConstruct
    public void init() {
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity configJournalAppEntity : all) {
            APP_CACHE.put(configJournalAppEntity.getKey(), configJournalAppEntity.getValue()) ;
        }
    }

}
