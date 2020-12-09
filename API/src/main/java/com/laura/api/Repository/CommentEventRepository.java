package com.laura.api.repository;

import com.laura.api.model.CommentEvent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEventRepository extends CrudRepository<CommentEvent, Long>{
    
}
