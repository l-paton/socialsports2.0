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
	
	@PutMapping("/edit/firstname")
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

	@PutMapping("/edit/description")
	public ResponseEntity<?> editDescription(@RequestBody String description){
		User user = getUser();
		user.setDescription(description);
		try{
			userService.editUser(user);
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/edit/address")
	public ResponseEntity<?> editAddress(@RequestBody String address){
		User user = getUser();
		user.setAddress(address);
		try{
			userService.editUser(user);
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/edit/gender")
	public ResponseEntity<?> editGender(@RequestBody String gender){
		User user = getUser();
		user.setGender(gender);
		try{
			userService.editUser(user);
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser(){
		try{
			userService.deleteUser(getUser());
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/add/friend/{id}")
	public ResponseEntity<?> addFriend(@PathVariable("id") long id){
		try{
			userService.addFriend(getUser(), id);
			return ResponseEntity.ok().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/delete/friend/{id}")
	public ResponseEntity<?> deleteFriend(@PathVariable("id") long id){
		try{
			userService.deleteFriend(getUser(), id);
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/friends")
	public ResponseEntity<?> getFriends(){
		try{
			return ResponseEntity.ok(getUser().getFriends());
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/events/joined")
	public ResponseEntity<?> getEventsJoined(){
		try{
			return ResponseEntity.ok(getUser().getEventsJoined().stream().filter(o -> !o.isFinish()));
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/events/joined/finished")
	public ResponseEntity<?> getEventsJoinedFinished(){
		try{
			return ResponseEntity.ok(getUser().getEventsJoined().stream().filter(o -> o.isFinish()));
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/picture")
	public ResponseEntity<?> getPicture(){
		return ResponseEntity.ok(getUser().getPicture());
	}

	@GetMapping("/data")
	public User getUserData(){
		return getUser();
	}
	
	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
	
}
