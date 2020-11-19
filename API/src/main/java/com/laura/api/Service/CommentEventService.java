package com.laura.api.Service;

import java.util.Date;

import com.laura.api.Repository.CommentEventRepository;
import com.laura.api.model.CommentEvent;
import com.laura.api.model.Event;
import com.laura.api.model.User;

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
}
