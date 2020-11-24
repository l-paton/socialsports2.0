package com.laura.api.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class FriendshipId implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @ManyToOne
    private User userOne;

    @ManyToOne
    private User userTwo;

    public FriendshipId(){

    }

    public FriendshipId(User userOne, User userTwo) {
        this.userOne = userOne;
        this.userTwo = userTwo;
    }

    public User getUserOne() {
        return userOne;
    }

    public void setUserOne(User userOne) {
        this.userOne = userOne;
    }

    public User getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(User userTwo) {
        this.userTwo = userTwo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userOne == null) ? 0 : userOne.hashCode());
        result = prime * result + ((userTwo == null) ? 0 : userTwo.hashCode());
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
        FriendshipId other = (FriendshipId) obj;
        if (userOne == null) {
            if (other.userOne != null)
                return false;
        } else if (!userOne.equals(other.userOne))
            return false;
        if (userTwo == null) {
            if (other.userTwo != null)
                return false;
        } else if (!userTwo.equals(other.userTwo))
            return false;
        return true;
    }
    
}
