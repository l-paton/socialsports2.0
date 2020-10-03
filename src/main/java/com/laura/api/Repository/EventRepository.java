package com.laura.api.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.laura.api.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>{
	
}
