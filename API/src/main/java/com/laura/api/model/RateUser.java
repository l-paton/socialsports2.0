package com.laura.api.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class RateUser {
    
    @EmbeddedId RateId rateId;

    private float score;

    public RateUser(){
        
    }

    public RateUser(RateId rateId, float score) {
        this.rateId = rateId;
        this.score = score;
    }

    public RateId getRateId() {
        return rateId;
    }

    public void setRateId(RateId rateId) {
        this.rateId = rateId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

}
