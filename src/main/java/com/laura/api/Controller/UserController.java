package com.laura.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laura.api.Service.UserService;
import com.laura.api.model.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;
	
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
	
	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
	
}
