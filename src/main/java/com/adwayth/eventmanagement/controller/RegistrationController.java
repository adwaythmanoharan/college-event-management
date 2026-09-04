package com.adwayth.eventmanagement.controller;

import com.adwayth.eventmanagement.entity.Registration;
import com.adwayth.eventmanagement.repository.RegistrationRepository;
import com.adwayth.eventmanagement.repository.StudentRepository;
import com.adwayth.eventmanagement.repository.SubEventRepository;
import com.adwayth.eventmanagement.repository.EventRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RegistrationController {

    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final EventRepository eventRepository;
    private final SubEventRepository subEventRepository;

    public RegistrationController(
            RegistrationRepository registrationRepository,
            StudentRepository studentRepository,
            EventRepository eventRepository,SubEventRepository subEventRepository) {

        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.eventRepository = eventRepository;
        this.subEventRepository=subEventRepository;
    }

    @GetMapping("/registrations")
    public List<Registration> getRegistrations() {
        return registrationRepository.findAll();
    }

    @PostMapping("/registrations")
    public ResponseEntity<?> addRegistration(
            @RequestBody Registration registration) {

        if (!studentRepository.existsById(registration.getStudentId())) {
            return ResponseEntity.badRequest()
                    .body("Student does not exist");
        }

        if (!eventRepository.existsById(registration.getEventId())) {
            return ResponseEntity.badRequest()
                    .body("Event does not exist");
        }

        if (!subEventRepository.existsById(registration.getSubEventId())) {
    return ResponseEntity.badRequest()
            .body("Sub-event does not exist");
}

        return ResponseEntity.ok(
                registrationRepository.save(registration)
        );
    }

    @DeleteMapping("/registrations/{id}")
    public String deleteRegistration(@PathVariable int id) {

        registrationRepository.deleteById(id);

        return "Registration deleted successfully";
    }
}