package com.laura.api.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laura.api.model.CommentEvent;
import com.laura.api.model.Event;
import com.laura.api.model.User;
import com.laura.api.payload.SearchRequest;
import com.laura.api.repository.EventRepository;

@Service
public class EventService {

	@Autowired
	EventRepository repository;

	@Autowired
	UserService userService;

	public Set<Event> getEventsNotFinished() {
		return repository.findByFinish(false);
	}
	
	public Iterable<Event> getEvents() {
		return repository.findAll();
	}

	public Event getEvent(long id){
		return repository.findById(id).orElse(null);
	}

	public Set<Event> getEventsByOrganizer(User user){
		return repository.findByOrganizer(user);
	}

	public Event createEvent(Event event) {
		return repository.save(event);
	}

	public void deleteEvent(Event event) {
		repository.delete(event);
	}

	public Event sendRequestToJoinEvent(long id, User user) {
		Event event = repository.findById(id).orElse(null);
		
		if (event != null) {

			if (event.getMaxParticipants() == 0 || event.getMaxParticipants() > event.getParticipants().size()) {

				Set<User> set = event.getApplicants();
				set.add(user);
				event.setApplicants(set);
				
				return repository.save(event);
			}
		}

		return null;
	}

	public Event leaveEvent(long id, User user) {
		Event event = repository.findById(id).orElse(null);

		if (event != null) {
			Set<User> set = event.getParticipants();
			set.remove(user);
			event.setParticipants(set);
			return repository.save(event);
		}

		return null;
	}

	public void deleteParticipant(Event event, long idUser){
		Set<User> participants = event.getParticipants();
		User participant = null;

		for(User user : participants){
			if(user.getId() == idUser){
				participant = user;
				break;
			}
		}

		if(participant != null) participants.remove(participant);

		repository.save(event);
	}

	public Set<Event> getApplicantsToUserEvents(User user) {
		return repository.findByOrganizer(user).stream().filter(o -> o.getApplicants().size() > 0).collect(Collectors.toSet());
	
	}

	public void acceptApplicant(Event event, long idUser) {

		User user = userService.getUserById(idUser);

		if (event != null && user != null) {
			Set<User> set = event.getParticipants();
			set.add(user);
			event.setParticipants(set);

			if (repository.save(event) != null) {
				cancelRequest(event.getId(), idUser);
			}
		}
	}

	public void denyApplicant(long id, long idUser) {
		cancelRequest(id, idUser);
	}

	public Set<Event> searchEvents(SearchRequest searchRequest) {
		Set<Event> events = (HashSet<Event>) repository.findAll().stream().filter(o -> !o.isFinish()).collect(Collectors.toSet());

		if (searchRequest.getSport() != null && searchRequest.getSport() != "") {
			events.removeIf(o -> !o.getSport().equalsIgnoreCase(searchRequest.getSport()));
		}

		if (searchRequest.getAddress() != null && searchRequest.getAddress() != "") {
			events.removeIf(o -> !o.getAddress().equalsIgnoreCase(searchRequest.getAddress()));
		}

		if (searchRequest.getstartDate() != null) {
			events.removeIf(o -> o.getStartDate() != searchRequest.getStartDate());
		}

		if (searchRequest.getTime() != null && searchRequest.getTime() != "") {
			events.removeIf(o -> !o.getTime().equals(searchRequest.getTime()));
		}

		if(searchRequest.getScore() > 0){
			Iterator<Event> iter = events.iterator();
			while (iter.hasNext()) {
				Event e = iter.next();
			
				if (e.getOrganizer().getReputationOrganizer() < searchRequest.getScore())
					iter.remove();
			}
		}
		
		return events;
	}

	public void cancelRequest(long id, long idUser) {
		Event event = repository.findById(id).orElse(null);
		User user = userService.getUserById(idUser);

		if (event != null && user != null) {
			Set<User> set = event.getApplicants();
			set.remove(user);
			event.setApplicants(set);
			repository.save(event);
		}
	}

	public Event editEvent(Event event){
		return repository.save(event);
	}

	public void publishEvent(long idEvent, User user, String comment){
		Event event = getEvent(idEvent);
		if(event != null){
			CommentEvent commentEvent = new CommentEvent(event, user, comment, new Date(System.currentTimeMillis()));
			event.getUserComments().add(commentEvent);
			repository.save(event);
		}
	}

}
