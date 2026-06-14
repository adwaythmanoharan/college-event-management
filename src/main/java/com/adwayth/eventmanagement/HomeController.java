package com.adwayth.eventmanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

@RestController
public class HomeController {

    @GetMapping("/event")
    public Event getEvent() {
        Event e = new Event();

        e.eventid=1;
        e.eventName="Tech Fest";
        e.venue="MCA HALL";

        return e;
    }
    @GetMapping ("/events")
    public ArrayList<Event> getEvents()
    {
        ArrayList<Event> events= new ArrayList<>();

        Event e1= new Event();
        e1.eventid=1;
        e1.eventName="Onam";
        e1.venue="MCA HALL";

        Event e2= new Event();
        e2.eventid=2;
        e2.eventName="Vishu";
        e2.venue="MBA HALL";

        events.add(e1);
        events.add(e2);

        return events;
    }
}