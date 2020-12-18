package com.laura.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import com.laura.api.model.User;
import com.laura.api.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	PasswordEncoder encoder;
	
	public User getUser(String email) {
		return repository.findByEmail(email).orElse(null);
	}

	public User getUserById(long id){
		return repository.findById(id).orElse(null);
	}
	
	public User editUser(User user) {
		return repository.save(user);
	}
	
	public void deleteUser(User user) {
		repository.delete(user);
	}
	
	public Set<User> getUsers(){
		return repository.findAll();
	}

	public boolean editPassword(User user, String password){
		if(password.length() > 6 && password.length() < 1024){
			user.setPassword(encoder.encode(password));
			repository.save(user);
			return true;
		}
		return false;
	}

}
