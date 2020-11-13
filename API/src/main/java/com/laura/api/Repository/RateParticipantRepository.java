package com.laura.api.Repository;

import java.util.Set;

import com.laura.api.model.RateParticipant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateParticipantRepository extends CrudRepository<RateParticipant,Long>{
    Set<RateParticipant> findByRateIdIdVoted(long id);
}
