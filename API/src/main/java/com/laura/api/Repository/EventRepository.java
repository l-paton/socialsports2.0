package com.laura.api.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

import com.laura.api.model.Event;
import com.laura.api.model.User;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>{

	@Override
    Set<Event> findAll();

	Set<Event> findByStartDateAfter(Date date);

	Set<Event> findByFinish(boolean bool);

	Set<Event> findByOrganizer(User user);

	Set<Event> findBySport(String sport);

	Set<Event> findByAddress(String address);

	Set<Event> findByStartDate(Date startDate);

	Set<Event> findByTime(String time);
}
