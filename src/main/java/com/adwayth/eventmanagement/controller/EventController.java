package com.adwayth.eventmanagement.controller;

import com.adwayth.eventmanagement.entity.Event;
import com.adwayth.eventmanagement.repository.EventRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/events")
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/events/{id}")
    public Event getEventById(@PathVariable int id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isPresent()) {
            return event.get();
        }

        return null;
    }

    @PostMapping("/events")
    public Event addEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PutMapping("/events/{id}")
    public Event updateEvent(@PathVariable int id, @RequestBody Event event) {

        Event existingEvent = eventRepository.findById(id).orElse(null);

        if (existingEvent == null) {
            return null;
        }

        existingEvent.setEventName(event.getEventName());
        existingEvent.setVenue(event.getVenue());

        return eventRepository.save(existingEvent);
    }

    @DeleteMapping("/events/{id}")
    public String deleteEvent(@PathVariable int id) {

        eventRepository.deleteById(id);

        return "Event deleted successfully";
    }
}