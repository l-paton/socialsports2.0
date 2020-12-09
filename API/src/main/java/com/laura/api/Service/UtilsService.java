package com.laura.api.service;

import com.laura.api.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UtilsService {
    
    @Autowired
    UserService userService;

    public User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
}
