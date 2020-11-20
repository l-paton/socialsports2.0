package com.laura.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laura.api.Repository.UserRepository;
import com.laura.api.model.User;

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
	
	public Iterable<User> getUsers(){
		return repository.findAll();
	}

	public boolean editPassword(User user, String password){
		if(password.length() > 6){
			user.setPassword(encoder.encode(password));
			repository.save(user);
			return true;
		}
		return false;
	}

}
