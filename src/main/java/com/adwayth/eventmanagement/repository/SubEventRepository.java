package com.adwayth.eventmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adwayth.eventmanagement.entity.SubEvent;

public interface SubEventRepository extends JpaRepository<SubEvent, Integer> {

    List<SubEvent> findByEventId(int eventId);


}
