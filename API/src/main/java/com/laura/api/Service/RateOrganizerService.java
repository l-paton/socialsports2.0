package com.laura.api.Service;

import java.util.Set;

import com.laura.api.Repository.RateOrganizerRepository;
import com.laura.api.model.RateOrganizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateOrganizerService {
    
    @Autowired
    RateOrganizerRepository repository;

    public RateOrganizer insertVote(RateOrganizer rateOrganizer){
        return repository.save(rateOrganizer);
    }

    public float getScoreOrganizer(long idOrganizer){
        Set<RateOrganizer> set = repository.findByRateIdIdVoted(idOrganizer);

        float finalScore = 0;

        for(RateOrganizer rp : set){
            finalScore += rp.getScore();
        }

        if(set.size() > 0) return finalScore/set.size();
        
        return 0;
    }
}
