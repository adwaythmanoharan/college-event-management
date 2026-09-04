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

import jakarta.servlet.http.HttpSession;

import com.adwayth.eventmanagement.entity.Registration;
import com.adwayth.eventmanagement.entity.Student;



@Controller
public class PageController {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;

    public PageController(EventRepository eventRepository,RegistrationRepository registrationRepository,StudentRepository studentRepository)
    {
        this.eventRepository=eventRepository;
        this.registrationRepository=registrationRepository;
        this.studentRepository=studentRepository;
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
    public String registrationPage(@org.springframework.web.bind.annotation.RequestParam int eventId,
Model model,HttpSession session){

     Integer studentId = (Integer) session.getAttribute("studentId");

                 if (studentId == null) {
        return "redirect:/";
    }

        Event event = eventRepository.findById(eventId).orElse(null);

        model.addAttribute("event", event);

        return "register";



    }

    @PostMapping("/register")
public String submitRegistration(
        @RequestParam int eventId,
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

}