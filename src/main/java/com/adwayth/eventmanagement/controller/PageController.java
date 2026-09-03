package com.adwayth.eventmanagement.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.adwayth.eventmanagement.entity.Event;
import com.adwayth.eventmanagement.repository.EventRepository;

@Controller
public class PageController {

    private final EventRepository eventRepository;

    public PageController(EventRepository eventRepository)
    {
        this.eventRepository=eventRepository;
    }

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/event-list")
    public String eventListPage(Model model){

        List<Event> events= eventRepository.findAll();

        model.addAttribute("events", events);

        return "event-list";
    }
    


}