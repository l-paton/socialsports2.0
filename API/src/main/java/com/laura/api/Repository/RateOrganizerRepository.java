package com.laura.api.Repository;

import java.util.Set;

import com.laura.api.model.RateOrganizer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateOrganizerRepository extends CrudRepository<RateOrganizer, Long>{
    Set<RateOrganizer> findByRateIdIdVoted(long id);
}
