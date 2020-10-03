package com.laura.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.api.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository repository;
}
