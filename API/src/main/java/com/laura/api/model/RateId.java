package com.laura.api.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class RateId implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private long idVoted;
    private long idVoter;
    private long idEvent;

    public RateId(){
        
    }

    public RateId(long idVoted, long idVoter, long idEvent) {
        this.idVoted = idVoted;
        this.idVoter = idVoter;
        this.idEvent = idEvent;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public long getIdVoted() {
        return idVoted;
    }

    public void setIdVoted(long idVoted) {
        this.idVoted = idVoted;
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
}
