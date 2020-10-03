package com.laura.api.Controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laura.api.Service.EventService;
import com.laura.api.model.Event;

@RestController
@RequestMapping("/api/event")
public class EventController {

	@Autowired
	EventService eventService;
	
	@PostMapping("/create")
	public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event, Errors errors){
		if(!errors.hasErrors()) {
			event.setCreatedAt(new Date(System.currentTimeMillis()));;
			event.setFinish(false);
			return ResponseEntity.ok(eventService.createEvent(event));
		}else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/list")
	public Iterable<Event> getEvents(){
		return eventService.getEvents();
	}
}
