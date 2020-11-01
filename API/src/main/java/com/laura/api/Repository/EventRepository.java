package com.laura.api.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import com.laura.api.model.Event;
import com.laura.api.model.User;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>{
	List<Event> findByStartDateAfter(Date date);

	List<Event> findByFinish(boolean bool);

	List<Event> findByOrganizer(User user);
}
