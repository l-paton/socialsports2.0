package com.laura.api.Service;


import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.api.Repository.EventRepository;
import com.laura.api.model.Event;
import com.laura.api.model.User;

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
	
	public Event joinToEvent(long id, User user) {
		Event event = repository.findById(id).orElse(null);
		
		if(event != null) {
			Set<User> set = event.getParticipants();
			set.add(user);
			event.setParticipants(set);
			return repository.save(event);
		}
		
		return null;
	}

	public Event leaveEvent(long id, User user){
		Event event = repository.findById(id).orElse(null);

		if(event != null){
			Set<User> set = event.getParticipants();
			set.remove(user);
			event.setParticipants(set);
			return repository.save(event);
		}

		return null;
	}
}
