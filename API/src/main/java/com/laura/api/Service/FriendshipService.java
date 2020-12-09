package com.laura.api.service;

import java.util.HashSet;
import java.util.Set;

import com.laura.api.model.Friendship;
import com.laura.api.model.FriendshipId;
import com.laura.api.model.User;
import com.laura.api.repository.FriendshipRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendshipService {
    
    @Autowired
    FriendshipRepository repository;

    @Autowired
    UserService userService;

    public void sendRequest(User user, long id){
        User receiver = userService.getUserById(id);

        if(receiver != null){
            if(!getFriends(user).contains(receiver)){
                System.out.println("Si");
                FriendshipId fi = new FriendshipId(user, receiver);
                Friendship friendship = new Friendship(fi, 0);
                repository.save(friendship);
            }
            System.out.println("No");
        }
    }

    public void cancelRequest(User user, long id){
        User receiver = userService.getUserById(id);

        if(receiver != null){
            FriendshipId fi = new FriendshipId(user, receiver);
            repository.deleteById(fi);
        }
    }

    public Set<User> getMyRequestsSent(User user){
        Set<Friendship> friendships = repository.findByFriendshipIdUserOneId(user.getId());
        Set<User> requestsSent = new HashSet<>();
        if(friendships != null){
            for(Friendship fs : friendships){
                if(fs.getStatus() == 0){
                    User receiver = fs.getFriendshipId().getUserTwo();
                    requestsSent.add(receiver);
                }
            }
        }
        return requestsSent;
    }

    public Set<User> getMyRequestsReceived(User user){
        Set<Friendship> friendships = repository.findByFriendshipIdUserTwoId(user.getId());
        Set<User> requestsReceived = new HashSet<>();
        if(friendships != null){
            for(Friendship fs : friendships){
                if(fs.getStatus() == 0){
                    User sender = fs.getFriendshipId().getUserOne();
                    requestsReceived.add(sender);
                }
            }
        }
        return requestsReceived;
    }

    public void acceptRequest(User user, long id){
        User sender = userService.getUserById(id);

        if(sender != null){
            FriendshipId fi = new FriendshipId(sender, user);
            Friendship friendship = repository.findByFriendshipId(fi);
            friendship.setStatus(1);
            repository.save(friendship);
        }
    }

    public void deleteFriend(User user, long id){
        User friend = userService.getUserById(id);

        if(friend != null){
            FriendshipId fi1 = new FriendshipId(friend, user);
            FriendshipId fi2 = new FriendshipId(user, friend);
            Friendship friendship = repository.findByFriendshipId(fi1);
            if(friendship == null) friendship = repository.findByFriendshipId(fi2);
            if(friendship != null) repository.delete(friendship);
        }
    }

    public void denyRequest(User user, long id){
        User sender = userService.getUserById(id);

        if(sender != null){
            FriendshipId fi = new FriendshipId(sender, user);
            Friendship friendship = repository.findByFriendshipId(fi);
            repository.delete(friendship);
        }
    }

    public Set<User> getFriends(User user){
        Set<Friendship> f1 = repository.findByFriendshipIdUserOneId(user.getId());
        Set<Friendship> f2 = repository.findByFriendshipIdUserTwoId(user.getId());

        Set<User> friends = new HashSet<>();

        if(f1 != null){
            for(Friendship friendship : f1){
                if(friendship.getStatus() == 1){
                    friends.add(friendship.getFriendshipId().getUserTwo());
                }
            }
        }

        if(f2 != null){
            for(Friendship friendship : f2){
                if(friendship.getStatus() == 1){
                    friends.add(friendship.getFriendshipId().getUserOne());
                }
            }
        }

        return friends;
    }
}
