package com.laura.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laura.api.Service.UserService;
import com.laura.api.model.User;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping("/list")
	public Iterable<User> getUsers(){
		return userService.getUsers();
	}
	
	@PutMapping("/edit/fistname")
	public ResponseEntity<?> editFirstName(@RequestBody String firstName){
		
		User user = getUser();
		user.setFirstName(firstName);
		if(userService.editUser(user) != null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping("/edit/lastname")
	public ResponseEntity<?> editLastName(@RequestBody String lastName){
		User user = getUser();
		user.setLastName(lastName);
		if(userService.editUser(user) != null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser(){
		User user = getUser();
		userService.deleteUser(user);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/add/friend/{id}")
	public ResponseEntity<?> addFriend(@PathVariable("id") long id){
		userService.addFriend(getUser(), id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/friends")
	public Iterable<User> getFriends(){
		return userService.getFriends(getUser().getId());
	}
	
	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
	
}
