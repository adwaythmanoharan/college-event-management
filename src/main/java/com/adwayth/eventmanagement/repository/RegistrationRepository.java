package com.adwayth.eventmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adwayth.eventmanagement.entity.Registration;



public interface RegistrationRepository extends JpaRepository<Registration,Integer>
{

    List<Registration> findByStudentId(int studentId);
    
}