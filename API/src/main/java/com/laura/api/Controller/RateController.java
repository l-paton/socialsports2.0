package com.laura.api.Controller;

import com.laura.api.Service.RateOrganizerService;
import com.laura.api.Service.RateParticipantService;
import com.laura.api.Service.UserService;
import com.laura.api.model.RateId;
import com.laura.api.model.RateOrganizer;
import com.laura.api.model.RateParticipant;
import com.laura.api.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/rate")
@CrossOrigin(origins = "*")
public class RateController {
    
    @Autowired
    RateParticipantService rateParticipantService;

    @Autowired
    RateOrganizerService rateOrganizerService;

    @Autowired
    UserService userService;

    @PostMapping("/participant")
    public ResponseEntity<?> rateParticipant(@RequestParam long idParticipant, @RequestParam long idEvent, @RequestParam float score){
        try{
            RateId rateId = new RateId(idParticipant, getUser().getId(), idEvent);
            RateParticipant rp = new RateParticipant(rateId, score);

            if(rateParticipantService.insertVote(rp) != null){
                User participant = userService.getUserById(idParticipant);
                participant.setReputationParticipant(rateParticipantService.getScoreParticipant(idParticipant));
                userService.editUser(participant);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    
    @PostMapping("/organizer")
    public ResponseEntity<?> rateOrganizer(@RequestParam long idOrganizer, @RequestParam long idEvent, @RequestParam float score){
        try{
            RateId rateId = new RateId(idOrganizer, getUser().getId(), idEvent);
            RateOrganizer rp = new RateOrganizer(rateId, score);

            if(rateOrganizerService.insertVote(rp) != null){
                User organizer = userService.getUserById(idOrganizer);
                organizer.setReputationOrganizer(rateOrganizerService.getScoreOrganizer(idOrganizer));
                userService.editUser(organizer);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
}
