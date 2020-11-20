package com.laura.api.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.api.Repository.EventRepository;
import com.laura.api.model.CommentEvent;
import com.laura.api.model.Event;
import com.laura.api.model.User;
import com.laura.api.payload.SearchRequest;

@Service
public class EventService {

	@Autowired
	EventRepository repository;

	public Iterable<Event> getEvents() {
		return repository.findAll();
	}

	public Event getEvent(long id) {
		return repository.findById(id).orElse(null);
	}

	public Set<Event> getEventsByOrganizer(User user){
		return repository.findByOrganizer(user);
	}

	public Set<Event> getEventsNotFinished() {
		return repository.findByFinish(false);
	}

	public Event createEvent(Event event) {
		return repository.save(event);
	}

	public void deleteEvent(Event event) {

		Set<User> setParticipants = event.getParticipants();
		Set<User> setApplicants = event.getApplicants();

		for (User u : setParticipants) {
			event = leaveEvent(event.getId(), u);
		}

		for(User u: setApplicants){
			event = cancelRequest(event.getId(), u);
		}

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

	public void acceptApplicant(Event event, User user) {

		if (event != null) {
			Set<User> set = event.getParticipants();
			set.add(user);
			event.setParticipants(set);

			if (repository.save(event) != null) {
				cancelRequest(event.getId(), user);
			}
		}
	}

	public void denyApplicant(long id, User user) {
		cancelRequest(id, user);
	}

	public Set<Event> searchEvents(SearchRequest searchRequest) {
		System.out.println(searchRequest.toString());
		Set<Event> events = (HashSet<Event>) repository.findAll().stream().filter(o -> !o.isFinish()).collect(Collectors.toSet());

		if (searchRequest.getSport() != null && searchRequest.getSport() != "") {
			Set<Event> findBySport = (HashSet<Event>) repository.findBySport(searchRequest.getSport());
			if (findBySport != null)
				events.retainAll(findBySport);
		}

		if (searchRequest.getAddress() != null && searchRequest.getAddress() != "") {
			Set<Event> findByAddress = (HashSet<Event>) repository.findByAddress(searchRequest.getAddress());
			if (findByAddress != null)
				events.retainAll(findByAddress);
		}

		if (searchRequest.getstartDate() != null) {
			Set<Event> findByStartDate = (HashSet<Event>) repository.findByStartDate(searchRequest.getstartDate());
			if (findByStartDate != null)
				events.retainAll(findByStartDate);
		}

		if (searchRequest.getTime() != null && searchRequest.getTime() != "") {
			Set<Event> findByTime = (HashSet<Event>) repository.findByTime(searchRequest.getTime());
			if (findByTime != null)
				events.retainAll(findByTime);
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

	public Event cancelRequest(long id, User user) {
		Event event = repository.findById(id).orElse(null);

		if (event != null) {
			Set<User> set = event.getApplicants();
			set.remove(user);
			event.setApplicants(set);
			return repository.save(event);
		}

		return null;
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
