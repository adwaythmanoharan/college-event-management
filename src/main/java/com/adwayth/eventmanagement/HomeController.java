package com.adwayth.eventmanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class HomeController {
    private final EventRepository eventRepository;
    private final StudentRepository studentRepository;

    public HomeController(EventRepository eventRepository,
        StudentRepository studentRepository
    )
{
    this.eventRepository = eventRepository;
    this.studentRepository=studentRepository;

}

/*not needed since we have already moved to the findAll
    @GetMapping("/event")
    public Event getEvent() {
        Event e = new Event();

        // e.eventid=1;
        // e.eventName="Tech Fest";
        // e.venue="MCA HALL";

        return e;
    }
        */

    @GetMapping ("/events")
    public List<Event> getEvents()
    {
        return eventRepository.findAll();
    }

    @GetMapping("/events/{id}")
    public Event getEventById(@PathVariable int id)
    {
    Optional<Event> event = eventRepository.findById(id);

    if(event.isPresent())
    {
        return event.get();
    }

    return null;
    }

    @GetMapping("/students")
    public List<Student> getStudent()
    {
        return studentRepository.findAll();
    }

    
}