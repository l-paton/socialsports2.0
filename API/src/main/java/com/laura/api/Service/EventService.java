package com.laura.api.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.api.Repository.EventRepository;
import com.laura.api.model.Event;
import com.laura.api.model.User;
import com.laura.api.payload.SearchRequest;

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

		Set<User> set = event.getParticipants();

		for(User u : set){
			event = leaveEvent(event.getId(), u);
			System.out.println(event.toString());
		}

		repository.delete(event);
	}
	
	public Event sendRequestToJoinEvent(long id, User user) {
		Event event = repository.findById(id).orElse(null);
		
		if(event != null) {
			Set<User> set = event.getApplicants();
			set.add(user);
			event.setApplicants(set);;
			return repository.save(event);
		}
		
		return null;
	}

	public Event cancelRequest(long id, User user){
		Event event = repository.findById(id).orElse(null);

		if(event != null){
			Set<User> set = event.getApplicants();
			set.remove(user);
			event.setApplicants(set);
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

	public HashMap<Long, User> getApplicantsToUserEvents(User user){
		Set<Event> listEvent = (HashSet<Event>)repository.findByOrganizer(user);
		HashMap<Long, User> map = new HashMap<>();

		for(Event event : listEvent){
			if(event.getApplicants().size() > 0){
				for(User applicant : event.getApplicants()){
					map.put(event.getId(), applicant);
				}
			}
		}
		return map;
	}

	
	public void acceptApplicant(long id, User user){
		Event event = repository.findById(id).orElse(null);
		
		if(event != null) {
			Set<User> set = event.getParticipants();
			set.add(user);
			event.setParticipants(set);
			
			if(repository.save(event) != null){
				cancelRequest(id, user);
			}
		}
	}

	public void denyApplicant(long id, User user){
		cancelRequest(id, user);
	}

	public Set<Event> getEventsNotFinished(){
		return repository.findByFinish(false);
	}

	public Set<Event> searchEvents(SearchRequest searchRequest){
		
		Set<Event> events = new HashSet<>();

		if(searchRequest.getSport() != null && searchRequest.getSport() != ""){
			Set<Event> findBySport = (HashSet<Event>)repository.findBySport(searchRequest.getSport());
			if(findBySport != null && findBySport.size() > 0) events.addAll(findBySport);
		}
		if(searchRequest.getAddress() != null && searchRequest.getAddress() != ""){
			Set<Event> findByAddress = (HashSet<Event>)repository.findByAddress(searchRequest.getAddress());
			if(findByAddress != null && findByAddress.size() > 0) events.addAll(findByAddress);
		}

		if(searchRequest.getstartDate() != null){
			System.out.println(searchRequest.getstartDate());
			Set<Event> findByStartAt = (HashSet<Event>)repository.findByStartDate(searchRequest.getstartDate());
			if(findByStartAt != null && findByStartAt.size() > 0) events.addAll(findByStartAt);
		}
		if(searchRequest.getTime() != null && searchRequest.getTime() != ""){
			Set<Event> findByTime = (HashSet<Event>)repository.findByTime(searchRequest.getTime());
			if(findByTime != null && findByTime.size() > 0) events.addAll(findByTime);
		}

		events = events.stream().filter(o -> !o.isFinish()).collect(Collectors.toSet());
		
		return events;
	}
}
