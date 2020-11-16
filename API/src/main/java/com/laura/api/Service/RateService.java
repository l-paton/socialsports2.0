package com.laura.api.Service;

import java.util.Set;
import java.util.stream.Collectors;

import com.laura.api.Repository.RateRepository;
import com.laura.api.model.Event;
import com.laura.api.model.RateId;
import com.laura.api.model.RateUser;
import com.laura.api.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {
    
    @Autowired
    RateRepository repository;

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    public void insertVote(long idVoted, User voter, long idEvent, float score, int type){
        User userVoted = userService.getUserById(idVoted);
        Event event = eventService.getEvent(idEvent);

        if(userVoted != null && event != null){
            
            RateId rateId = new RateId(userVoted, voter, event, type);
            RateUser rp;

            if(event.getOrganizer().getId() == userVoted.getId() && type == 1){
                rp = new RateUser(rateId, score);
                repository.save(rp);
                userVoted.setReputationOrganizer(getScore(idVoted, type));
                userService.editUser(userVoted);
            }else if(type == 2){
                rp = new RateUser(rateId, score);
                repository.save(rp);
                userVoted.setReputationParticipant(getScore(idVoted, type));
                userService.editUser(userVoted);
            }
        }
    }

    public float getScore(long idUser, int type){
        Set<RateUser> set = repository.findByRateIdVotedId(idUser).stream().filter(o -> o.getRateId().getType() == type).collect(Collectors.toSet());

        float finalScore = 0;

        for(RateUser rp : set){
            finalScore += rp.getScore();
        }

        if(set.size() > 0) return finalScore/set.size();
        
        return 0;
        
    }
}
