package com.laura.api.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laura.api.Service.FriendshipService;
import com.laura.api.Service.UserService;
import com.laura.api.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/friend")
@CrossOrigin(origins = "*")
public class FriendshipController {

    @Autowired
    FriendshipService friendshipService;

    @Autowired
    UserService userService;
    
    @PostMapping("/sendrequest")
	public ResponseEntity<?> sendFriendRequest(@RequestParam("id") long id){
		try{
            
            friendshipService.sendRequest(getUser(), id);
            return ResponseEntity.ok().build();
            
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/accept")
	public ResponseEntity<?> acceptFriend(@RequestParam("id") long id){
		try{
            
            friendshipService.acceptRequest(getUser(), id);
            return ResponseEntity.ok().build();
            
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/deny")
	public ResponseEntity<?> denyFriend(@RequestParam("id") long id){
		try{
            
            friendshipService.denyRequest(getUser(), id);
            return ResponseEntity.noContent().build();
            
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteFriend(@PathVariable("id") long id){
		try{
            
            //

			return ResponseEntity.noContent().build();
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/list")
	public ResponseEntity<?> getMyFriends(){
		try{
            return ResponseEntity.ok(friendshipService.getFriends(getUser()));
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/list/{id}")
	public ResponseEntity<?> getFriends(@PathVariable long id){
		try{
            
            User user = userService.getUserById(id);
            if(user != null){
                return ResponseEntity.ok(friendshipService.getFriends(user));
            }

            return ResponseEntity.badRequest().build();
            
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/requests/sent")
	public ResponseEntity<?> getRequestsSent(){
		try{
            return ResponseEntity.ok(friendshipService.getMyRequestsSent(getUser()));
		}catch(Exception e){
			return ResponseEntity.badRequest().build();
		}
    }

    @GetMapping("/requests/received")
    public ResponseEntity<?> getRequestsReceived(){
        try{
            return ResponseEntity.ok(friendshipService.getMyRequestsReceived(getUser()));
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    
    private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
}
