package com.laura.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.laura.api.model.Event;
import com.laura.api.model.User;
import com.laura.api.service.UserService;
import com.laura.api.service.UtilsService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UtilsService utilsService;
	
	@GetMapping("/list")
	public Set<User> getUsers(){
		return userService.getUsers();
	}
	
	@PutMapping("/edit/firstname")
	public ResponseEntity<String> editFirstName(@RequestParam String firstName){
		if(firstName.length() > 0 && firstName.length() <= 32){
			User user = utilsService.getUser();
			user.setFirstName(firstName);
			if(userService.editUser(user) != null) {
				return ResponseEntity.noContent().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping("/edit/lastname")
	public ResponseEntity<String> editLastName(@RequestParam String lastName){
		if(lastName.length() > 0 && lastName.length() <= 32){
			User user = utilsService.getUser();
			user.setLastName(lastName);
			if(userService.editUser(user) != null) {
				return ResponseEntity.noContent().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("/edit/address")
	public ResponseEntity<String> editAddress(@RequestParam String address){
		if(address.length() <= 32){
			User user = utilsService.getUser();
			user.setAddress(address);
			if(userService.editUser(user) != null){
				return ResponseEntity.noContent().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("/edit/birthday")
	public ResponseEntity<String> editBirthday(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date birthday){
		if(birthday.before(new Date(System.currentTimeMillis()))){
			User user = utilsService.getUser();
			user.setBirthday(birthday);
			if(userService.editUser(user) != null){
				return ResponseEntity.noContent().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("/edit/gender")
	public ResponseEntity<String> editGender(@RequestParam String gender){
		User user = utilsService.getUser();
		if(gender.equalsIgnoreCase("mujer") || gender.equalsIgnoreCase("hombre")){
			user.setGender(gender);		
		}else{
			user.setGender(null);
		}
		
		return ResponseEntity.noContent().build();

	}

	@PutMapping("/edit/password")
	public ResponseEntity<String> editPassword(@RequestParam String password){
		if(userService.editPassword(utilsService.getUser(), password)){
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.badRequest().build();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteUser(){
		userService.deleteUser(utilsService.getUser());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/events/joined/{id}")
	public Set<Event> getEventsJoined(@PathVariable("id") long id){
		User user = userService.getUserById(id);
		return user.getEventsJoined();
	}

	@GetMapping("/events/joined/notfinished")
	public Set<Event> getEventsJoinedNotFinished(){
		return utilsService.getUser().getEventsJoined().stream().filter(o -> !o.isFinish()).collect(Collectors.toSet());
	}

	@GetMapping("/events/joined/finished")
	public Set<Event> getEventsJoinedFinished(){
		return utilsService.getUser().getEventsJoined().stream().filter(o -> o.isFinish()).collect(Collectors.toSet());
	}

	@GetMapping("/events/applied")
	public Set<Event> getEventsApplied(){
		Set<Event> events = utilsService.getUser().getEventsApplied();
		return events;
	}

	@GetMapping("/picture")
	public ResponseEntity<String> getPicture(){
		return ResponseEntity.ok(utilsService.getUser().getPicture());
	}

	@GetMapping("/data")
	public User getUserData(){
		return utilsService.getUser();
	}
	
	@GetMapping("/profile/{id}")
	public ResponseEntity<User> getUserProfile(@PathVariable long id){
		User user = userService.getUserById(id);
		if(user != null){
			return ResponseEntity.ok(user);
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/email")
	public ResponseEntity<String> getEmail(){
		return ResponseEntity.ok(utilsService.getUser().getEmail());
	}
}
