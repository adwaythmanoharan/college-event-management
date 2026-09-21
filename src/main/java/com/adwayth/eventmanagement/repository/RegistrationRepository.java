package com.adwayth.eventmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adwayth.eventmanagement.entity.Registration;



public interface RegistrationRepository
        extends JpaRepository<Registration, Integer> {

    List<Registration> findByStudentId(int studentId);

    List<Registration> findByEventId(int eventId);

    List<Registration> findByRegisteredAtBetween(
            LocalDateTime start,
            LocalDateTime end);

    long countBySubEventId(int subEventId);

    boolean existsByStudentIdAndSubEventId(
            int studentId,
            int subEventId);
}