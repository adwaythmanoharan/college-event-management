package com.adwayth.eventmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adwayth.eventmanagement.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer>
{

}


