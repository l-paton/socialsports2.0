package com.laura.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import com.laura.api.Repository.UserRepository;
import com.laura.api.model.User;

@Service
public class UserService {

	@Autowired
	UserRepository repository;
	
	public User getUser(String email) {
		return repository.findByEmail(email).orElse(null);
	}
	
	public User editUser(User user) {
		return repository.save(user);
	}
	
	public void deleteUser(User user) {
		repository.delete(user);
	}
	
	public Iterable<User> getUsers(){
		return repository.findAll();
	}

	public void addFriend(User user, long id){

		User friend = repository.findById(id).orElse(null);
		
		if(friend != null && user != null && friend.getId() != user.getId()) {
			Set<User> userSet = user.getFriends();
			Set<User> friendSet = friend.getFriends();
			
			userSet.add(friend);
			friendSet.add(user);

			user.setFriends(userSet);
			friend.setFriends(friendSet);

			repository.save(user);
			repository.save(friend);
		}
		
	}

	public void deleteFriend(User user, long id){
		User friend = repository.findById(id).orElse(null);
		
		if(friend != null && user != null){
			Set<User> userSet = user.getFriends();
			Set<User> friendSet = friend.getFriends();

			userSet.remove(friend);
			friendSet.remove(user);

			user.setFriends(userSet);
			friend.setFriends(friendSet);

			repository.save(user);
			repository.save(friend);
		}
	}

}
