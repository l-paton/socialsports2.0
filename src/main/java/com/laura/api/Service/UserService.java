package com.laura.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
