package com.laura.api.Controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.server.ResponseStatusException;

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
	CommentEventService commentEventService;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/create")
	public Event createEvent(@Valid @RequestBody Event event){
		
		try{
			User user = getUser();
			event.setOrganizer(user);
			event.setCreatedAt(new Date(System.currentTimeMillis()));
			Set<User> set = event.getParticipants();
			if(set == null) set = new HashSet<User>();	
			set.add(getUser());
			event.setParticipants(set);
			event.setFinish(false);

			return eventService.createEvent(event);

		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/search")
	public Set<Event> searchEvents(
		@RequestParam(required = false) String sport,
		@RequestParam(required = false) String address,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
		@RequestParam(required = false) String time,
		@RequestParam(required = false) float score){
			
		try{
			SearchRequest searchRequest = new SearchRequest(sport, address, startDate, time, score);
			Set<Event> events = (HashSet<Event>)eventService.searchEvents(searchRequest);
			return events;
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/list")
	public Set<Event> getEvents(){
		try{
			Set<Event> events = eventService.getEventsNotFinished();
			return events;
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/get/{id}")
	public Event getEvent(@PathVariable("id") long id) {
		try{
			Event event = eventService.getEvent(id);
			return event;
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/created")
	public Set<Event> getEventsCreatedByUser(){
		try{
			Set<Event> events = eventService.getEventsByOrganizer(getUser());
			return events;
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	/* ===============================
		PARTICIPANTES Y SOLICITANTES
	 ===============================*/

	@PostMapping("/accept")
	public ResponseEntity<String> acceptApplicantRequest(@RequestParam("idEvent") long idEvent, @RequestParam("idUser") long idUser){

		try{
			Event event = eventService.getEvent(idEvent);

			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				User user = userService.getUserById(idUser);
				if(user != null){
					eventService.acceptApplicant(event, user);
					return ResponseEntity.ok().build();
				}
			}

			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/deny")
	public ResponseEntity<String> denyApplicantRequest(@RequestParam("idEvent") long idEvent, @RequestParam("idUser") long idUser){

		try{
			Event event = eventService.getEvent(idEvent);

			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				User user = userService.getUserById(idUser);
				if(user != null){
					eventService.cancelRequest(idEvent, user);
					return ResponseEntity.ok().build();
				}
			}
			
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/join")
	public ResponseEntity<String> joinToEvent(@RequestParam("id") long id){

		try{
			if(eventService.sendRequestToJoinEvent(id, getUser()) != null) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/cancel/{id}")
	public ResponseEntity<String> cancelRequest(@PathVariable("id") long id){
		try{
			eventService.cancelRequest(id, getUser());
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/leave/{id}")
	public ResponseEntity<String> leaveEvent(@PathVariable("id") long id){
		try{
			Event event = eventService.leaveEvent(id, getUser());
			
			if(event.getParticipants().contains(getUser())){
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			
			return ResponseEntity.noContent().build();

		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/requests")
	public Set<Event> getRequests(){
		try{
			Set<Event> events = eventService.getApplicantsToUserEvents(getUser());
			return events;
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/removeparticipant/{idEvent}/{idUser}")
	public ResponseEntity<String> removeParticipant(@PathVariable("idEvent") long idEvent, @PathVariable("idUser") long idUser){
		try{
			Event event = eventService.getEvent(idEvent);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				eventService.deleteParticipant(event, idUser);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	/* ===============================
		 EDITAR DATOS DEL EVENTO
	 ===============================*/

	@PutMapping("/edit/address")
	public ResponseEntity<String> editAddress(@RequestParam("id") long id, @RequestParam("address") String address){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setAddress(address);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/startdate")
	public ResponseEntity<String> editStartDate(@RequestParam("id") long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setStartDate(startDate);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/edit/time")
	public ResponseEntity<String> editTime(@RequestParam("id") long id, @RequestParam("time") String time){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setTime(time);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/maxparticipants")
	public ResponseEntity<String> editMaxParticipants(@RequestParam("id") long id, @RequestParam("maxParticipants") int maxParticipants){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				if(event.getParticipants().size() > maxParticipants){
					return ResponseEntity.badRequest().build();
				}
				event.setMaxParticipants(maxParticipants);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/comment")
	public ResponseEntity<String> editComment(@RequestParam("id") long id, @RequestParam("comment") String comment){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setComments(comment);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/minage")
	public ResponseEntity<String> editMinAge(@RequestParam("id") long id, @RequestParam("minAge") int minAge){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.getRequirement().setMinAge(minAge);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/maxage")
	public ResponseEntity<String> editMaxage(@RequestParam("id") long id, @RequestParam("maxAge") int maxAge){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.getRequirement().setMaxAge(maxAge);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/gender")
	public ResponseEntity<String> editGender(@RequestParam("id") long id, @RequestParam("gender") String gender){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				if(gender.equals("empty")) gender = null;
				if(gender.equalsIgnoreCase("mujer") || gender.equalsIgnoreCase("hombre") || gender == null){
					event.getRequirement().setGender(gender);
					eventService.editEvent(event);
					return ResponseEntity.noContent().build();
				}else{
					return ResponseEntity.badRequest().build();
				}
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/finish")
	public ResponseEntity<String> editFinish(@RequestParam("id") long id){
		try{
			Event event = eventService.getEvent(id);
			if(event != null && event.getOrganizer().getId() == getUser().getId()){
				event.setFinish(true);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> removeEvent(@PathVariable("id") long id){
		Event event = eventService.getEvent(id);
		try{
			if(event.getOrganizer().getId() == getUser().getId()) {
				eventService.deleteEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}	
	}

	/* ===============================
		 COMENTARIOS EN EVENTO
	 ===============================*/

	@PostMapping("/publish/comment")
	public ResponseEntity<String> publishComment(@RequestParam("idEvent") long idEvent, @RequestParam("comment") String comment){
		try{
			Event event = eventService.getEvent(idEvent);

			if(event != null){
				commentEventService.saveNewComment(event, getUser(), comment);
				return ResponseEntity.ok().build();
			}

			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/comment/{idComment}")
	public ResponseEntity<String> deleteComment(@PathVariable("idComment") long idComment){
		try{
			if(commentEventService.deleteComment(getUser().getId(), idComment)){
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/comments/{id}")
	public ResponseEntity<?> getEventComments(@PathVariable("id") long id){
		try{
			Event event = eventService.getEvent(id);
			if(event != null){
				if(event.getParticipants().contains(getUser())) return ResponseEntity.ok(event.getUserComments());
				else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
}
