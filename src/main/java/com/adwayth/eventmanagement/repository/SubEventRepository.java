package com.adwayth.eventmanagement.repository;

import com.adwayth.eventmanagement.entity.SubEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubEventRepository
        extends JpaRepository<SubEvent, Integer> {

    List<SubEvent> findByEventId(int eventId);

    long countByEventId(int eventId);
}