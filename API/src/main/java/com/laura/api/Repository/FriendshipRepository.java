package com.laura.api.Repository;

import java.util.Set;

import com.laura.api.model.Friendship;
import com.laura.api.model.FriendshipId;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, FriendshipId>{
    
    Set<Friendship> findByFriendshipIdUserOneId(long id);

    Set<Friendship> findByFriendshipIdUserTwoId(long id);

    Friendship findByFriendshipId(FriendshipId friendshipId);
}
