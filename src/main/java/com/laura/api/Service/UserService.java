package com.laura.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public User addFriend(User user, long id){

		User friend = repository.findById(id).orElse(null);
		
		if(friend != null && user != null) {
			Set<User> set = user.getFriends();
			set.add(friend);
			user.setFriends(set);
			return repository.save(user);
		}
		return null;
	}

}
