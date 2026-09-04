package com.adwayth.eventmanagement.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.adwayth.eventmanagement.entity.Event;
import com.adwayth.eventmanagement.repository.EventRepository;
import com.adwayth.eventmanagement.repository.RegistrationRepository;
import com.adwayth.eventmanagement.repository.StudentRepository;
import com.adwayth.eventmanagement.repository.SubEventRepository;

import jakarta.servlet.http.HttpSession;

import com.adwayth.eventmanagement.entity.Registration;
import com.adwayth.eventmanagement.entity.Student;
import com.adwayth.eventmanagement.entity.SubEvent;



@Controller
public class PageController {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final SubEventRepository subEventRepository;

    public PageController(EventRepository eventRepository,RegistrationRepository registrationRepository,StudentRepository studentRepository,SubEventRepository subEventRepository
)
    {
        this.eventRepository=eventRepository;
        this.registrationRepository=registrationRepository;
        this.studentRepository=studentRepository;
        this.subEventRepository=subEventRepository;
    }

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

     @PostMapping ("/login")
    public String login(@RequestParam int studentId,@RequestParam String password,HttpSession session,Model model) {

        Student student= studentRepository.findById(studentId).orElse(null);

        if(student==null || !student.getPassword().equals(password))
        {
            model.addAttribute("error", "Invalid studentid or password");
        
        return "login";
        }

        session.setAttribute("studentId", student.getStudentId());

        return "redirect:/event-list";
    }

    @GetMapping("/event-list")
    public String eventListPage(HttpSession session,Model model){


        List<Event> events= eventRepository.findAll();

        model.addAttribute("events", events);

        Integer studentId=(Integer) session.getAttribute("studentId");

        if (studentId == null) {
        return "redirect:/";
    }

        model.addAttribute("studentId",studentId);


        return "event-list";
    }

    @GetMapping("/register")
    public String registrationPage(@org.springframework.web.bind.annotation.RequestParam int eventId, @RequestParam int subEventId,
Model model,HttpSession session){

     Integer studentId = (Integer) session.getAttribute("studentId");

                 if (studentId == null) {
        return "redirect:/";
    }

        Event event = eventRepository.findById(eventId).orElse(null);

        SubEvent subEvent =
        subEventRepository.findById(subEventId).orElse(null);

if (subEvent == null) {
    return "redirect:/event-list";
}
        model.addAttribute("subEvent", subEvent);
        model.addAttribute("event", event);

        return "register";



    }

    @PostMapping("/register")
public String submitRegistration(
        @RequestParam int eventId,@RequestParam int subEventId,
        HttpSession session,Model model) {

                Integer studentId = (Integer) session.getAttribute("studentId");

                 if (studentId == null) {
        return "redirect:/";
    }

            if (!studentRepository.existsById(studentId)) {

        Event event = eventRepository.findById(eventId).orElse(null);

        model.addAttribute("event", event);
        model.addAttribute("error", "Student does not exist");

        return "register";
    }

    Registration registration = new Registration();

    registration.setStudentId(studentId);
    registration.setEventId(eventId);
    registration.setSubEventId(subEventId);

    registrationRepository.save(registration);

    return "redirect:/registration-list";
}
    
@GetMapping("/registration-list")
public String registrationListPage(HttpSession session,Model model) {

    Integer studentId= (Integer) session.getAttribute("studentId");

    if(studentId==null)
    {
        return "redirect:/";
    }

    List<Registration> registrations = registrationRepository.findByStudentId(studentId);

    model.addAttribute("registrations", registrations);

    return "registration-list";
}

@GetMapping("/logout")
public String Logout(HttpSession session){

session.invalidate();

return "redirect:/";

}

@GetMapping("/subevents")
public String SubEvents(@RequestParam int eventId,HttpSession session,Model model){

    Integer studentId= (Integer) session.getAttribute("studentId");

    if(studentId==null)
    {
        return "redirect:/";
    }

     Event event = eventRepository.findById(eventId).orElse(null);

    if (event == null) {
        return "redirect:/event-list";
    }

    List<SubEvent> subEvents= subEventRepository.findByEventId(eventId);

    model.addAttribute("event", event);
    model.addAttribute("subEvents", subEvents);

    return "subevent-list";
}

}