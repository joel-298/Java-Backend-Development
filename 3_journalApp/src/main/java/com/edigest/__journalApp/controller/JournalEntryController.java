package com.edigest.__journalApp.controller;

import com.edigest.__journalApp.entity.JournalEntry;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap ; 
import java.util.Map ; 
import java.util.List ; 
import java.util.ArrayList; 


@RestController
@RequestMapping("/journal") 
public class JournalEntryController {

    private Map<Long,JournalEntry> journalEntries = new HashMap<>() ; 

    //1) GET
    @GetMapping
    public List<JournalEntry> getAll() {                      // '/journal' : GET
        return new ArrayList<>(journalEntries.values()) ; 
    }

    //2) GET BASED ON PARAMS 
    @GetMapping("id/{myId}") 
    public JournalEntry getJournalEntryById(@PathVariable Long myId) {
        return journalEntries.get(myId) ;
    } 


    //3) POST
    @PostMapping                                              // '/journal' : POST
    public boolean createEntry(@RequestBody JournalEntry myEntry) {
        // Journal Entry ka ek instance ban jae ga i.e myEntry
        journalEntries.put(myEntry.getId(), myEntry) ; 
        return true ; 
    }

    //4) DELETE 
    @DeleteMapping("id/{myId}") 
    public JournalEntry deleteHJournalEntry(@PathVariable Long myId) {
        return journalEntries.remove(myId) ; 
    }

    //5) UPDATE / PUT
    @PutMapping("id/{myId}")
    public JournalEntry updateJournalEntry(@PathVariable Long myId, @RequestBody JournalEntry myEntry) {
        return journalEntries.put(myId,myEntry) ;
    }

    // // 6) UPDATE / PATCH 
    // @PatchMapping("id/{myId}") 
    // public JournalEntry updateJournalEntry(@PathVariable Long myId, @RequestBody JournalEntry myEntry) {

    // }
}



// -) METHODS/FUNCTIONS INSDE A CONTROLLER CLASS SHOULD BE PUBLIC SO THAT THEY CAN BE ACCESSED AND INVOKED BY THE SPRING FRAMEWORK OR EXTERNAL HTTP REQUEST
// -) Select "raw" and "JSON" in the body of a POST request in POSTMAN indicates that the request body will contain in JSON format, allowing the server to parse and process
//    the incoming data accurately. This ensures that the data is transmitted and received in a structured manner, following the JSON conventions.
// -) RETURN SATEMENTS BEHAVES LIKE A RESPONSE : (in the code above they are returning old values !)


// 1) RESPONSE 
// {
//     "id": 1,
//     "title": "Joel Matthew",
//     "content": "SDE"
// },
// {
//     "id": 2,
//     "title": "Kashish Barthwal",
//     "content": "SDE"
// }


// 2) RESPONSE 
// {
//     "id": 2,
//     "title": "Kashish Barthwal",
//     "content": "SDE" 
// }