package com.laura.api.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.laura.api.model.User;
import com.laura.api.payload.JwtResponse;
import com.laura.api.payload.LoginRequest;
import com.laura.api.payload.MessageResponse;
import com.laura.api.payload.SignupRequest;
import com.laura.api.repository.UserRepository;
import com.laura.api.security.jwt.JwtUtils;
import com.laura.api.security.services.UserDetailsImpl;
import com.laura.api.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try{
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			String jwt = jwtUtils.generateJwtToken(loginRequest.getEmail());
			
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			return ResponseEntity.ok(new JwtResponse(jwt, userService.getUser(userDetails.getEmail())));

		}catch(Exception e){
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			
			return ResponseEntity.badRequest().body(new MessageResponse(errors.get(0).getDefaultMessage()));
		}

		try{
			if (userRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity
						.status(HttpStatus.CONFLICT)
						.body(new MessageResponse("Ese email ya está en uso"));
			}
			
			User user = new User(signUpRequest.getEmail(), signUpRequest.getFirstname(), signUpRequest.getLastname(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getGender(), new Date(System.currentTimeMillis()), signUpRequest.getBirthday());
			if(user.getFirstName() == null) user.setFirstName("sin nombre");
			user.setReputationParticipant(0);
			user.setReputationOrganizer(0);

			userRepository.save(user);
			return ResponseEntity.ok(new MessageResponse("Usuario registrado"));

		}catch(Exception e){
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}		
	}
}