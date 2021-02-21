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
	public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtils.generateJwtToken(loginRequest.getEmail());
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return new JwtResponse(jwt, userService.getUser(userDetails.getEmail()));
	}

	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			return new ResponseEntity<>(new MessageResponse(errors.get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>(new MessageResponse("Ese email ya está en uso"), HttpStatus.CONFLICT);
		}
		
		User user = new User(signUpRequest.getEmail(), signUpRequest.getFirstname(), signUpRequest.getLastname(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getGender(), new Date(System.currentTimeMillis()), signUpRequest.getBirthday());
		if(user.getFirstName() == null) user.setFirstName("sin nombre");
		user.setReputationParticipant(0);
		user.setReputationOrganizer(0);

		userRepository.save(user);
		return new ResponseEntity<>(new MessageResponse("Usuario registrado"), HttpStatus.OK);
	}
}