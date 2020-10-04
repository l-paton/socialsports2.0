package com.laura.api.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.api.Repository.EventRepository;
import com.laura.api.model.Event;

@Service
public class EventService {
	
	@Autowired
	EventRepository repository;
	
	public Iterable<Event> getEvents(){
		return repository.findAll();
	}
	
	public Event getEvent(long id) {
		return repository.findById(id).orElse(null);
	}
	
	public Event createEvent(Event event) {
		return repository.save(event);
	}
	
	public void deleteEvent(Event event) {
		repository.delete(event);
	}
}
