package com.adwayth.eventmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adwayth.eventmanagement.entity.Student;



public interface StudentRepository extends JpaRepository<Student,Integer>
{

    
}