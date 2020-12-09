package com.laura.api.service;

import java.util.Date;

import com.laura.api.model.CommentEvent;
import com.laura.api.model.Event;
import com.laura.api.model.User;
import com.laura.api.repository.CommentEventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentEventService {
    
    @Autowired
    CommentEventRepository repository;

    public CommentEvent saveNewComment(Event event, User user, String comment){
        CommentEvent commentEvent = new CommentEvent(event, user, comment, new Date(System.currentTimeMillis()));
        return repository.save(commentEvent);
    }

    public boolean deleteComment(long userId, long idComment){
        CommentEvent commentEvent = repository.findById(idComment).orElse(null);
        if(commentEvent != null){
            if(commentEvent.getUser().getId() == userId){
                repository.delete(commentEvent);
                return true;
            }
        }
        return false;
    }
}
