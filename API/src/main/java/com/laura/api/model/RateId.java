package com.laura.api.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class RateId implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @ManyToOne
    private User voted;

    @ManyToOne
    private User voter;

    @ManyToOne
    private Event event;

    private int type;

    public RateId(){
        
    }

    public RateId(User voted, User voter, Event event, int type) {
        this.voted = voted;
        this.voter = voter;
        this.event = event;
        this.type = type;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public User getVoted() {
        return voted;
    }

    public void setVoted(User voted) {
        this.voted = voted;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
