package com.laura.api.Controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laura.api.Repository.UserRepository;
import com.laura.api.Security.jwt.JwtUtils;
import com.laura.api.Security.services.UserDetailsImpl;
import com.laura.api.Service.UserService;
import com.laura.api.model.User;
import com.laura.api.payload.JwtResponse;
import com.laura.api.payload.LoginRequest;
import com.laura.api.payload.MessageResponse;
import com.laura.api.payload.SignupRequest;

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

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtils.generateJwtToken(loginRequest.getEmail());
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt, userService.getUser(userDetails.getEmail())));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, Errors errors) {
		
		System.out.println(signUpRequest.toString());

		if (errors.hasErrors()) {
			return ResponseEntity
					.badRequest()
					//cambiar mensaje
					.body("Rellenar bien el campo " + errors.getFieldError().getField());
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Ese email ya está en uso"));
		}
		
		User user = new User(signUpRequest.getEmail(), signUpRequest.getFirstname(), signUpRequest.getLastname(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getGender(), new Date(System.currentTimeMillis()), signUpRequest.getBirthday());
		
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}