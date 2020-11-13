package com.laura.api.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class RateId implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private long idParticipant;
    private long idVoter;
    private long idEvent;

    public RateId(){
        
    }

    public long getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(long idParticipant) {
        this.idParticipant = idParticipant;
    }

    public long getIdVoter() {
        return idVoter;
    }

    public void setIdVoter(long idVoter) {
        this.idVoter = idVoter;
    }

    public long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(long idEvent) {
        this.idEvent = idEvent;
    }

    public RateId(long idParticipant, long idVoter, long idEvent) {
        this.idParticipant = idParticipant;
        this.idVoter = idVoter;
        this.idEvent = idEvent;
    }
}
