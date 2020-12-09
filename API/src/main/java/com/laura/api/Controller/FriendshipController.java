package com.laura.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.laura.api.model.User;
import com.laura.api.service.FriendshipService;
import com.laura.api.service.UserService;
import com.laura.api.service.UtilsService;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/friend")
@CrossOrigin(origins = "*")
public class FriendshipController {

    @Autowired
    FriendshipService friendshipService;

	@Autowired
	UserService userService;

    @Autowired
    UtilsService utilsService;
    
    @PostMapping("/sendrequest")
	public ResponseEntity<String> sendFriendRequest(@RequestParam("id") long id){
		try{
            friendshipService.sendRequest(utilsService.getUser(), id);
            return ResponseEntity.noContent().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/cancelrequest")
	public ResponseEntity<String> cancelFriendRequest(@RequestParam("id") long id){
		try{

			friendshipService.cancelRequest(utilsService.getUser(), id);
			return ResponseEntity.noContent().build();
			
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}	

	@PostMapping("/accept")
	public ResponseEntity<String> acceptFriend(@RequestParam("id") long id){
		try{
            friendshipService.acceptRequest(utilsService.getUser(), id);
            return ResponseEntity.noContent().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/deny")
	public ResponseEntity<String> denyFriend(@RequestParam("id") long id){
		try{
            friendshipService.denyRequest(utilsService.getUser(), id);
            return ResponseEntity.noContent().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteFriend(@PathVariable("id") long id){
		try{
            friendshipService.deleteFriend(utilsService.getUser(), id);
			return ResponseEntity.noContent().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/list")
	public ResponseEntity<?> getMyFriends(){
		try{
            return ResponseEntity.ok(friendshipService.getFriends(utilsService.getUser()));
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/list/{id}")
	public ResponseEntity<?> getFriendsOfUser(@PathVariable long id){
		try{
            User user = userService.getUserById(id);
            if(user != null){
                return ResponseEntity.ok(friendshipService.getFriends(user));
            }
            return ResponseEntity.badRequest().build();
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/requests/sent")
	public ResponseEntity<?> getRequestsSent(){
		try{
            return ResponseEntity.ok(friendshipService.getMyRequestsSent(utilsService.getUser()));
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
    }

    @GetMapping("/requests/received")
    public ResponseEntity<?> getRequestsReceived(){
        try{
            return ResponseEntity.ok(friendshipService.getMyRequestsReceived(utilsService.getUser()));
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
