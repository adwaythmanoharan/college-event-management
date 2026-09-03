package com.adwayth.eventmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adwayth.eventmanagement.entity.Registration;



public interface RegistrationRepository extends JpaRepository<Registration,Integer>
{

    
}