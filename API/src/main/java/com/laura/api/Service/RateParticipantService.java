package com.laura.api.Service;

import java.util.Set;

import com.laura.api.Repository.RateParticipantRepository;
import com.laura.api.model.RateParticipant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateParticipantService {
    
    @Autowired
    RateParticipantRepository repository;

    public RateParticipant insertVote(RateParticipant rateParticipant){
        return repository.save(rateParticipant);
    }

    public float getScoreParticipant(long idParticipant){
        Set<RateParticipant> set = repository.findByRateIdIdVoted(idParticipant);

        float finalScore = 0;

        for(RateParticipant rp : set){
            finalScore += rp.getScore();
        }

        if(set.size() > 0) return finalScore/set.size();
        else return 0;
        
    }
}
