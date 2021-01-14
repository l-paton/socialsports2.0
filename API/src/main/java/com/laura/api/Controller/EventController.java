package com.laura.api.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.laura.api.service.CommentEventService;
import com.laura.api.service.EventService;
import com.laura.api.service.UtilsService;
import com.laura.api.model.Event;
import com.laura.api.model.User;
import com.laura.api.payload.MessageResponse;
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
	UtilsService utilsService;
	
	@PostMapping("/create")
	public Event createEvent(@Valid @RequestBody Event event){
		User user = utilsService.getUser();
		event.setOrganizer(user);
		event.setCreatedAt(new Date(System.currentTimeMillis()));
		Set<User> set = event.getParticipants();
		if(set == null) set = new HashSet<User>();	
		set.add(utilsService.getUser());
		event.setParticipants(set);
		event.setFinish(false);

		return eventService.createEvent(event);
	}

	@GetMapping("/search")
	public Set<Event> searchEvents(
		@RequestParam(required = false) String sport,
		@RequestParam(required = false) String address,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
		@RequestParam(required = false) String time,
		@RequestParam(required = false, defaultValue = "0") Float score){
		
		SearchRequest searchRequest = new SearchRequest(sport, address, startDate, time, score);
		return eventService.searchEvents(searchRequest);
	}
	
	@GetMapping("/list")
	public Set<Event> getEvents(){
		Set<Event> events = eventService.getEventsNotFinished();
		return events;
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<Object> getEvent(@PathVariable("id") long id) {
		Event event = eventService.getEvent(id);
		if(event != null) return new ResponseEntity<>(event, HttpStatus.OK);
		return new ResponseEntity<>(new MessageResponse("Evento no encontrado"), HttpStatus.NOT_FOUND);
	}

	@GetMapping("/created")
	public Set<Event> getEventsCreatedByUser(){
		Set<Event> events = eventService.getEventsByOrganizer(utilsService.getUser());
		return events;
	}

	/* ===============================
		PARTICIPANTES Y SOLICITANTES
	 ===============================*/

	@PostMapping("/accept")
	public ResponseEntity<String> acceptApplicantRequest(@RequestParam("idEvent") long idEvent, @RequestParam("idUser") long idUser){
		Event event = eventService.getEvent(idEvent);

		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				eventService.acceptApplicant(event, idUser);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@PostMapping("/deny")
	public ResponseEntity<String> denyApplicantRequest(@RequestParam("idEvent") long idEvent, @RequestParam("idUser") long idUser){
		Event event = eventService.getEvent(idEvent);

		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				eventService.cancelRequest(idEvent, idUser);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} 
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@PostMapping("/join")
	public ResponseEntity<String> joinToEvent(@RequestParam("id") long id){
		eventService.sendRequestToJoinEvent(id, utilsService.getUser());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/cancel/{id}")
	public ResponseEntity<String> cancelRequest(@PathVariable("id") long id){
		eventService.cancelRequest(id, utilsService.getUser().getId());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/leave/{id}")
	public ResponseEntity<String> leaveEvent(@PathVariable("id") long id){
		Event event = eventService.leaveEvent(id, utilsService.getUser());
		
		if(event.getParticipants().contains(utilsService.getUser())){
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/requests")
	public Set<Event> getRequests(){
		Set<Event> events = eventService.getApplicantsToUserEvents(utilsService.getUser());
		return events;
	}

	@DeleteMapping("/removeparticipant/{idEvent}/{idUser}")
	public ResponseEntity<String> removeParticipant(@PathVariable("idEvent") long idEvent, @PathVariable("idUser") long idUser){
		Event event = eventService.getEvent(idEvent);
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				eventService.deleteParticipant(event, idUser);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} 
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	/* ===============================
		 EDITAR DATOS DEL EVENTO
	 ===============================*/

	@PutMapping("/edit/address")
	public ResponseEntity<String> editAddress(@RequestParam("id") long id, @RequestParam("address") String address){
		Event event = eventService.getEvent(id);
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				event.setAddress(address);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@PutMapping("/edit/startdate")
	public ResponseEntity<?> editStartDate(@RequestParam("id") long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate){
		Event event = eventService.getEvent(id);
		
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				event.setStartDate(startDate);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}
	
	@PutMapping("/edit/time")
	public ResponseEntity<?> editTime(@RequestParam("id") long id, @RequestParam("time") String time){
		Event event = eventService.getEvent(id);
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				event.setTime(time);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}

	@PutMapping("/edit/maxparticipants")
	public ResponseEntity<?> editMaxParticipants(@RequestParam("id") long id, @RequestParam("maxParticipants") int maxParticipants){
		Event event = eventService.getEvent(id);
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				if(event.getParticipants().size() > maxParticipants){
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("El tamaño máximo de participantes no puede ser menor que el total de participantes actuales."));
				}
				event.setMaxParticipants(maxParticipants);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}

	@PutMapping("/edit/comment")
	public ResponseEntity<?> editComment(@RequestParam("id") long id, @RequestParam("comment") String comment){
		Event event = eventService.getEvent(id);
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				event.setComments(comment);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}
			
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}

	@PutMapping("/edit/minage")
	public ResponseEntity<?> editMinAge(@RequestParam("id") long id, @RequestParam("minAge") int minAge){
		Event event = eventService.getEvent(id);

		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				event.getRequirement().setMinAge(minAge);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} 
			
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}

	@PutMapping("/edit/maxage")
	public ResponseEntity<?> editMaxage(@RequestParam("id") long id, @RequestParam("maxAge") int maxAge){
		Event event = eventService.getEvent(id);
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				event.getRequirement().setMaxAge(maxAge);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} 
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}

	@PutMapping("/edit/price")
	public ResponseEntity<?> editPrice(@RequestParam("id") long id, @RequestParam("price") float price){
		Event event = eventService.getEvent(id);
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				event.setPrice(price);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}

	@PutMapping("/edit/gender")
	public ResponseEntity<?> editGender(@RequestParam("id") long id, @RequestParam("gender") String gender){
		Event event = eventService.getEvent(id);
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				if(gender.equals("empty")) gender = null;
				if(gender.equalsIgnoreCase("mujer") || gender.equalsIgnoreCase("hombre") || gender == null){
					event.getRequirement().setGender(gender);
					eventService.editEvent(event);
					return ResponseEntity.noContent().build();
				}else{
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Genero inválido"));
				}
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} 
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}

	@PutMapping("/finish")
	public ResponseEntity<?> editFinish(@RequestParam("id") long id){
		Event event = eventService.getEvent(id);
		if(event != null){
			if(event.getOrganizer().getId() == utilsService.getUser().getId()){
				event.setFinish(true);
				eventService.editEvent(event);
				return ResponseEntity.noContent().build();
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> removeEvent(@PathVariable("id") long id){
		Event event = eventService.getEvent(id);
		if(event.getOrganizer().getId() == utilsService.getUser().getId()) {
			eventService.deleteEvent(event);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	/* ===============================
		 COMENTARIOS EN EVENTO
	 ===============================*/

	@PostMapping("/publish/comment")
	public ResponseEntity<?> publishComment(@RequestParam("idEvent") long idEvent, @RequestParam("comment") String comment){
		Event event = eventService.getEvent(idEvent);

		if(event != null){
			commentEventService.saveNewComment(event, utilsService.getUser(), comment);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}

	@DeleteMapping("/delete/comment/{idComment}")
	public ResponseEntity<String> deleteComment(@PathVariable("idComment") long idComment){
		if(commentEventService.deleteComment(utilsService.getUser().getId(), idComment)){
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.badRequest().build();
	}

	@GetMapping("/comments/{id}")
	public ResponseEntity<?> getEventComments(@PathVariable("id") long id){
		Event event = eventService.getEvent(id);
		if(event != null){
			if(event.getParticipants().contains(utilsService.getUser())) return ResponseEntity.ok(event.getUserComments());
			else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
	}
}
