package com.laura.api.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.springframework.web.server.ResponseStatusException;

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

	private final Logger logger = Logger.getLogger(EventController.class.getName());
	
	@PostMapping("/create")
	public Event createEvent(@Valid @RequestBody Event event){
		
		try{
			User user = utilsService.getUser();
			event.setOrganizer(user);
			event.setCreatedAt(new Date(System.currentTimeMillis()));
			Set<User> set = event.getParticipants();
			if(set == null) set = new HashSet<User>();	
			set.add(utilsService.getUser());
			event.setParticipants(set);
			event.setFinish(false);

			return eventService.createEvent(event);

		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/search")
	public Set<Event> searchEvents(
		@RequestParam(required = false) String sport,
		@RequestParam(required = false) String address,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
		@RequestParam(required = false) String time,
		@RequestParam(required = false, defaultValue = "0") Float score){
			
		try{
			SearchRequest searchRequest = new SearchRequest(sport, address, startDate, time, score);
			return eventService.searchEvents(searchRequest);
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/list")
	public Set<Event> getEvents(){
		try{
			Set<Event> events = eventService.getEventsNotFinished();
			return events;
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getEvent(@PathVariable("id") long id) {
		try{
			Event event = eventService.getEvent(id);
			if(event != null) return ResponseEntity.ok(event);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/created")
	public Set<Event> getEventsCreatedByUser(){
		try{
			Set<Event> events = eventService.getEventsByOrganizer(utilsService.getUser());
			return events;
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
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

			if(event != null){
				if(event.getOrganizer().getId() == utilsService.getUser().getId()){
					eventService.acceptApplicant(event, idUser);
					return ResponseEntity.noContent().build();
				}else{
					return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
				}
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/deny")
	public ResponseEntity<String> denyApplicantRequest(@RequestParam("idEvent") long idEvent, @RequestParam("idUser") long idUser){

		try{
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
			
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/join")
	public ResponseEntity<String> joinToEvent(@RequestParam("id") long id){
		try{
			eventService.sendRequestToJoinEvent(id, utilsService.getUser());
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/cancel/{id}")
	public ResponseEntity<String> cancelRequest(@PathVariable("id") long id){
		try{
			eventService.cancelRequest(id, utilsService.getUser().getId());
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/leave/{id}")
	public ResponseEntity<String> leaveEvent(@PathVariable("id") long id){
		try{
			Event event = eventService.leaveEvent(id, utilsService.getUser());
			
			if(event.getParticipants().contains(utilsService.getUser())){
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			
			return ResponseEntity.noContent().build();

		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/requests")
	public Set<Event> getRequests(){
		try{
			Set<Event> events = eventService.getApplicantsToUserEvents(utilsService.getUser());
			return events;
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/removeparticipant/{idEvent}/{idUser}")
	public ResponseEntity<String> removeParticipant(@PathVariable("idEvent") long idEvent, @PathVariable("idUser") long idUser){
		try{
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
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
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
			
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/startdate")
	public ResponseEntity<?> editStartDate(@RequestParam("id") long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate){
		try{
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
			
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/edit/time")
	public ResponseEntity<?> editTime(@RequestParam("id") long id, @RequestParam("time") String time){
		try{
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

		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/maxparticipants")
	public ResponseEntity<?> editMaxParticipants(@RequestParam("id") long id, @RequestParam("maxParticipants") int maxParticipants){
		try{
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
			
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/comment")
	public ResponseEntity<?> editComment(@RequestParam("id") long id, @RequestParam("comment") String comment){
		try{
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
			
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/minage")
	public ResponseEntity<?> editMinAge(@RequestParam("id") long id, @RequestParam("minAge") int minAge){
		try{
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
			
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/maxage")
	public ResponseEntity<?> editMaxage(@RequestParam("id") long id, @RequestParam("maxAge") int maxAge){
		try{
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

		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/price")
	public ResponseEntity<?> editPrice(@RequestParam("id") long id, @RequestParam("price") float price){
		try{
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
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/gender")
	public ResponseEntity<?> editGender(@RequestParam("id") long id, @RequestParam("gender") String gender){
		try{
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
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/finish")
	public ResponseEntity<?> editFinish(@RequestParam("id") long id){
		try{
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
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> removeEvent(@PathVariable("id") long id){
		Event event = eventService.getEvent(id);
		try{
			if(event.getOrganizer().getId() == utilsService.getUser().getId()) {
				eventService.deleteEvent(event);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}	
	}

	/* ===============================
		 COMENTARIOS EN EVENTO
	 ===============================*/

	@PostMapping("/publish/comment")
	public ResponseEntity<?> publishComment(@RequestParam("idEvent") long idEvent, @RequestParam("comment") String comment){
		try{
			Event event = eventService.getEvent(idEvent);

			if(event != null){
				commentEventService.saveNewComment(event, utilsService.getUser(), comment);
				return ResponseEntity.ok().build();
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
		}catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/comment/{idComment}")
	public ResponseEntity<String> deleteComment(@PathVariable("idComment") long idComment){
		try{
			if(commentEventService.deleteComment(utilsService.getUser().getId(), idComment)){
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
				if(event.getParticipants().contains(utilsService.getUser())) return ResponseEntity.ok(event.getUserComments());
				else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evento no encontrado"));
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
}
