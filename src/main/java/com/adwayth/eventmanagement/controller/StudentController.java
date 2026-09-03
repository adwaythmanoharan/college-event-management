package com.adwayth.eventmanagement.controller;

import com.adwayth.eventmanagement.entity.Student;
import com.adwayth.eventmanagement.repository.StudentRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students")
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }
}