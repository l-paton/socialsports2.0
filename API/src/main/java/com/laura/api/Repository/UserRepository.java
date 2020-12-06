package com.laura.api.Repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.laura.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	@Override
	Set<User> findAll();

	Optional<User> findByEmail(String email);
	
	Boolean existsByEmail(String email);
}
