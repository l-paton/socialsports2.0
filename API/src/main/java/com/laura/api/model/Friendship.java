package com.laura.api.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Friendship {
    
    @EmbeddedId
    FriendshipId friendshipId;

    private int status;

    public Friendship(){

    }

    public Friendship(FriendshipId friendshipId, int status) {
        this.friendshipId = friendshipId;
        this.status = status;
    }

    public FriendshipId getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(FriendshipId friendshipId) {
        this.friendshipId = friendshipId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
