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
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.laura.api.model.Event;
import com.laura.api.model.User;
import com.laura.api.payload.MessageResponse;
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

	private final Logger logger = Logger.getLogger(UserController.class.getName());
	
	@GetMapping("/list")
	public Set<User> getUsers(){
		try{
			return userService.getUsers();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/edit/firstname")
	public ResponseEntity<String> editFirstName(@RequestParam String firstName){
		try{
			if(firstName.length() > 0 && firstName.length() <= 32){
				User user = utilsService.getUser();
				user.setFirstName(firstName);
				if(userService.editUser(user) != null) {
					return ResponseEntity.noContent().build();
				}
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/edit/lastname")
	public ResponseEntity<String> editLastName(@RequestParam String lastName){
		try{
			if(lastName.length() > 0 && lastName.length() <= 32){
				User user = utilsService.getUser();
				user.setLastName(lastName);
				if(userService.editUser(user) != null) {
					return ResponseEntity.noContent().build();
				}
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	/*@PutMapping("/edit/description")
	public ResponseEntity<String> editDescription(@RequestParam String description){
		try{
			User user = getUser();
			user.setDescription(description);
			if(userService.editUser(user) != null){
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}*/

	@PutMapping("/edit/address")
	public ResponseEntity<String> editAddress(@RequestParam String address){
		try{
			if(address.length() <= 32){
				User user = utilsService.getUser();
				user.setAddress(address);
				if(userService.editUser(user) != null){
					return ResponseEntity.noContent().build();
				}
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/birthday")
	public ResponseEntity<String> editBirthday(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date birthday){
		try{
			if(birthday.before(new Date(System.currentTimeMillis()))){
				User user = utilsService.getUser();
				user.setBirthday(birthday);
				if(userService.editUser(user) != null){
					return ResponseEntity.noContent().build();
				}
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/gender")
	public ResponseEntity<String> editGender(@RequestParam String gender){
		try{
			User user = utilsService.getUser();
			if(gender.equalsIgnoreCase("mujer") || gender.equalsIgnoreCase("hombre")){
				user.setGender(gender);		
			}else{
				user.setGender(null);
			}
			if(userService.editUser(user) != null){
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/password")
	public ResponseEntity<String> editPassword(@RequestParam String password){
		try{
			if(userService.editPassword(utilsService.getUser(), password)){
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().body("La contraseña debe tener un mínimo de 6 carácteres");
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteUser(){
		try{
			userService.deleteUser(utilsService.getUser());
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/events/joined/{id}")
	public Set<Event> getEventsJoined(@PathVariable("id") long id){
		try{
			User user = userService.getUserById(id);
			return user.getEventsJoined();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/events/joined/notfinished")
	public Set<Event> getEventsJoinedNotFinished(){
		try{
			return utilsService.getUser().getEventsJoined().stream().filter(o -> !o.isFinish()).collect(Collectors.toSet());
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/events/joined/finished")
	public Set<Event> getEventsJoinedFinished(){
		try{
			return utilsService.getUser().getEventsJoined().stream().filter(o -> o.isFinish()).collect(Collectors.toSet());
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/events/applied")
	public Set<Event> getEventsApplied(){
		try{
			Set<Event> events = utilsService.getUser().getEventsApplied();
			return events;
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/picture")
	public ResponseEntity<String> getPicture(){
		try{
			return ResponseEntity.ok(utilsService.getUser().getPicture());
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/data")
	public User getUserData(){
		return utilsService.getUser();
	}
	
	@GetMapping("/profile/{id}")
	public ResponseEntity<?> getUserProfile(@PathVariable long id){
		try{
			User user = userService.getUserById(id);
			if(user != null){
				return ResponseEntity.ok(user);
			}else{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Usuario no encontrado"));
			}
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/email")
	public ResponseEntity<String> getEmail(){
		try{
			return ResponseEntity.ok(utilsService.getUser().getEmail());
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
}
