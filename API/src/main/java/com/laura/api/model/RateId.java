package com.laura.api.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        result = prime * result + type;
        result = prime * result + ((voted == null) ? 0 : voted.hashCode());
        result = prime * result + ((voter == null) ? 0 : voter.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RateId other = (RateId) obj;
        if (event == null) {
            if (other.event != null)
                return false;
        } else if (!event.equals(other.event))
            return false;
        if (type != other.type)
            return false;
        if (voted == null) {
            if (other.voted != null)
                return false;
        } else if (!voted.equals(other.voted))
            return false;
        if (voter == null) {
            if (other.voter != null)
                return false;
        } else if (!voter.equals(other.voter))
            return false;
        return true;
    }

}
