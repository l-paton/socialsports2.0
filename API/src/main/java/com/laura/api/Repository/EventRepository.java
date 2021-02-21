package com.laura.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
	
    @Query("select s from Event s "
    		+ "where (:sport is null or s.sport LIKE %:sport%) "
    		+ "and (:address is null or s.address LIKE %:address%) "
    		+ "and (:startDate is null or s.startDate = :startDate)"
    		+ "and (:time is null or s.time = :time)"
    		+ "and finish = false")
	Set<Event> findByOptionalSportAndOptionalAddressAndOptionalStartDateAndOptionalTime(@Param("sport") String sport, @Param("address") String address, @Param("startDate") Date startDate, @Param("time") String time);
}
