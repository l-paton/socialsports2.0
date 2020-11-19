package com.laura.api.Controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laura.api.Service.CommentEventService;
import com.laura.api.Service.EventService;
import com.laura.api.Service.UserService;
import com.laura.api.model.CommentEvent;
import com.laura.api.model.Event;
import com.laura.api.model.User;
import com.laura.api.payload.SearchRequest;


@RestController
@RequestMapping("/api/event")
@CrossOrigin(origins = "*")
public class EventController {

	@Autowired
	EventService eventService;

	@Autowired
	CommentEventService CommentEventService;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createEvent(@Valid @RequestBody Event event){
		
		try{
			System.out.println(event.toString());
			User user = getUser();
			event.setOrganizer(user);
			event.setCreatedAt(new Date(System.currentTimeMillis()));
			Set<User> set = event.getParticipants();
			if(set == null) set = new HashSet<User>();	
			set.add(getUser());
			event.setParticipants(set);
			event.setFinish(false);

			return ResponseEntity.ok(eventService.createEvent(event));

		}catch(Exception e){
			System.out.println(e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/list")
	public ResponseEntity<?> getEvents(){
		try{
			return ResponseEntity.ok(eventService.getEventsNotFinished());
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
		
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getEvent(@PathVariable("id") long id) {
		Event event = eventService.getEvent(id);
		
		if(event != null) {
			return ResponseEntity.ok(event);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> removeEvent(@PathVariable("id") long id){
		Event event = eventService.getEvent(id);
		try{
			if(event.getOrganizer().getId() == getUser().getId()) {
				eventService.deleteEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}	
	}

	@PostMapping("/accept")
	public ResponseEntity<?> acceptApplicantRequest(@RequestParam("idEvent") long idEvent, @RequestParam("idUser") long idUser){

		try{
			Event event = eventService.getEvent(idEvent);

			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				User user = userService.getUserById(idUser);
				if(user != null){
					eventService.acceptApplicant(event, user);
					return ResponseEntity.ok().build();
				}
			}

			return ResponseEntity.badRequest().build();
			
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/deny")
	public ResponseEntity<?> denyApplicantRequest(@RequestParam("idEvent") long idEvent, @RequestParam("idUser") long idUser){

		try{
			Event event = eventService.getEvent(idEvent);

			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				User user = userService.getUserById(idUser);
				if(user != null){
					eventService.cancelRequest(idEvent, user);
					return ResponseEntity.ok().build();
				}
			}

			return ResponseEntity.badRequest().build();
			
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/join")
	public ResponseEntity<?> joinToEvent(@RequestParam("id") long id){
		System.out.println(id);
		try{
			if(eventService.sendRequestToJoinEvent(id, getUser()) != null) {
				return ResponseEntity.noContent().build();
			}

			return ResponseEntity.badRequest().build();

		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/cancel/{id}")
	public ResponseEntity<?> cancelRequest(@PathVariable("id") long id){
		try{
			eventService.cancelRequest(id, getUser());
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/leave/{id}")
	public ResponseEntity<?> leaveEvent(@PathVariable("id") long id){
		try{
			Event event = eventService.leaveEvent(id, getUser());
			if(event.getParticipants().contains(getUser())){
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			
			return ResponseEntity.noContent().build();

		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/requests")
	public ResponseEntity<?> getRequests(){
		try{
			Set<Event> events = eventService.getApplicantsToUserEvents(getUser());
			return ResponseEntity.ok(events);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/removeparticipant/{idEvent}/{idUser}")
	public ResponseEntity<?> removeParticipant(@PathVariable("idEvent") long idEvent, @PathVariable("idUser") long idUser){
		try{
			Event event = eventService.getEvent(idEvent);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				eventService.deleteParticipant(event, idUser);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchEvents(
		@RequestParam(required = false) String sport,
		@RequestParam(required = false) String address,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
		@RequestParam(required = false) String time,
		@RequestParam(required = false) float score){
			System.out.println(score);
		try{
			SearchRequest searchRequest = new SearchRequest(sport, address, startDate, time, score);
			Set<Event> events = (HashSet<Event>)eventService.searchEvents(searchRequest);
			return ResponseEntity.ok(events);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/created")
	public ResponseEntity<?> getEventsCreatedByUser(){
		try{
			return ResponseEntity.ok(eventService.getEventsByOrganizer(getUser()));
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/edit/address")
	public ResponseEntity<?> editAddress(@RequestParam("id") long id, @RequestParam String address){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setAddress(address);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/edit/startdate")
	public ResponseEntity<?> editStartDate(@RequestParam("id") long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setStartDate(startDate);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/edit/time")
	public ResponseEntity<?> editTime(@RequestParam("id") long id, @RequestParam String time){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setTime(time);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/edit/maxparticipants")
	public ResponseEntity<?> editMaxParticipants(@RequestParam("id") long id, @RequestParam int maxParticipants){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setMaxParticipants(maxParticipants);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/finish")
	public ResponseEntity<?> editFinish(@RequestParam("id") long id){
		System.out.println("se mete");
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setFinish(true);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/publish/comment")
	public ResponseEntity<?> publishComment(@RequestParam("idEvent") long idEvent, @RequestParam("comment") String comment){
		try{
			Event event = eventService.getEvent(idEvent);

			if(event != null){
				//CommentEvent commentEvent = CommentEventService.saveNewComment(event, getUser(), comment);
				eventService.publishEvent(idEvent, getUser(), comment);
				return ResponseEntity.ok().build();
			}

			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/comments/{id}")
	public ResponseEntity<?> getEventComments(@PathVariable("id") long id){
		try{
			Event event = eventService.getEvent(id);
			if(event != null){
				return ResponseEntity.ok(event.getComments());
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
}
