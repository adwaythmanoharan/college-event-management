package com.adwayth.eventmanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {
    private final EventRepository eventRepository;

    public HomeController(EventRepository eventRepository)
{
    this.eventRepository = eventRepository;
}
    @GetMapping("/event")
    public Event getEvent() {
        Event e = new Event();

        // e.eventid=1;
        // e.eventName="Tech Fest";
        // e.venue="MCA HALL";

        return e;
    }
    @GetMapping ("/events")
    public List<Event> getEvents()
    {
        return eventRepository.findAll();
    }

    
}