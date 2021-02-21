package com.laura.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laura.api.model.User;
import com.laura.api.service.FriendshipService;
import com.laura.api.service.UserService;
import com.laura.api.service.UtilsService;

import java.util.Set;

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
        friendshipService.sendRequest(utilsService.getUser(), id);
        return ResponseEntity.noContent().build();
	}

	@PostMapping("/cancelrequest")
	public ResponseEntity<String> cancelFriendRequest(@RequestParam("id") long id){
		friendshipService.cancelRequest(utilsService.getUser(), id);
		return ResponseEntity.noContent().build();
	}	

	@PostMapping("/accept")
	public ResponseEntity<String> acceptFriend(@RequestParam("id") long id){
        friendshipService.acceptRequest(utilsService.getUser(), id);
        return ResponseEntity.noContent().build();
	}

	@PostMapping("/deny")
	public ResponseEntity<String> denyFriend(@RequestParam("id") long id){
        friendshipService.denyRequest(utilsService.getUser(), id);
        return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteFriend(@PathVariable("id") long id){
        friendshipService.deleteFriend(utilsService.getUser(), id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/list")
	public Set<User> getMyFriends(){
		return friendshipService.getFriends(utilsService.getUser());
	}

	@GetMapping("/list/{id}")
	public ResponseEntity<Set<User>> getFriendsOfUser(@PathVariable long id){
        User user = userService.getUserById(id);
        if(user != null){
            return ResponseEntity.ok(friendshipService.getFriends(user));
        }
        return ResponseEntity.badRequest().build();
	}

	@GetMapping("/requests/sent")
	public Set<User> getRequestsSent(){
		return friendshipService.getMyRequestsSent(utilsService.getUser());
    }

    @GetMapping("/requests/received")
    public Set<User> getRequestsReceived(){
    	return friendshipService.getMyRequestsReceived(utilsService.getUser());
    }
}
