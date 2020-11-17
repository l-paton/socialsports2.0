package com.laura.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import com.laura.api.Service.UserService;
import com.laura.api.model.User;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping("/list")
	public ResponseEntity<?> getUsers(){
		try{
			return ResponseEntity.ok(userService.getUsers());
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/edit/firstname")
	public ResponseEntity<?> editFirstName(@RequestParam String firstName){
		try{
			User user = getUser();
			user.setFirstName(firstName);
			if(userService.editUser(user) != null) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/edit/lastname")
	public ResponseEntity<?> editLastName(@RequestParam String lastName){
		try{
			User user = getUser();
			user.setLastName(lastName);
			if(userService.editUser(user) != null) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/edit/description")
	public ResponseEntity<?> editDescription(@RequestParam String description){
		try{
			User user = getUser();
			user.setDescription(description);
			if(userService.editUser(user) != null){
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/edit/address")
	public ResponseEntity<?> editAddress(@RequestParam String address){
		System.out.println(address);
		try{
			User user = getUser();
			user.setAddress(address);
			if(userService.editUser(user) != null){
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/edit/birthday")
	public ResponseEntity<?> editBirthday(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date birthday){
		try{
			User user = getUser();
			user.setBirthday(birthday);
			if(userService.editUser(user) != null){
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/edit/gender")
	public ResponseEntity<?> editGender(@RequestParam String gender){
		try{
			User user = getUser();
			user.setGender(gender);	
			if(userService.editUser(user) != null){
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
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

	@GetMapping("/events/joined/{id}")
	public ResponseEntity<?> getEventsJoined(@PathVariable("id") long id){
		try{
			User user = userService.getUserById(id);
			return ResponseEntity.ok(user.getEventsJoined());
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/events/joined/notfinished")
	public ResponseEntity<?> getEventsJoinedNotFinished(){
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

	@GetMapping("/events/applied")
	public ResponseEntity<?> getEventsApplied(){
		try{
			return ResponseEntity.ok(getUser().getEventsApplied());
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
	
	@GetMapping("/profile/{id}")
	public ResponseEntity<?> getUserProfile(@PathVariable long id){
		try{
			User user = userService.getUserById(id);
			if(user != null){
				return ResponseEntity.ok(user);
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
	
}
