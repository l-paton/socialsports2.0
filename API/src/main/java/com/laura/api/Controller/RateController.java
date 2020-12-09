package com.laura.api.controller;

import com.laura.api.service.RateService;
import com.laura.api.service.UtilsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/api/rate")
@CrossOrigin(origins = "*")
public class RateController {
    
    @Autowired
    RateService RateService;

    @Autowired
	UtilsService utilsService;

    @PostMapping("/participant")
    public ResponseEntity<String> rateParticipant(@RequestParam long idParticipant, @RequestParam long idEvent, @RequestParam float score){
        try{
            RateService.insertVote(idParticipant, utilsService.getUser(), idEvent, score, 2);
            return ResponseEntity.ok().build();
            
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/organizer")
    public ResponseEntity<String> rateOrganizer(@RequestParam long idOrganizer, @RequestParam long idEvent, @RequestParam float score){
        try{
            RateService.insertVote(idOrganizer, utilsService.getUser(), idEvent, score, 1);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
