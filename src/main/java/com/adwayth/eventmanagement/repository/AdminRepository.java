package com.adwayth.eventmanagement.repository;

import com.adwayth.eventmanagement.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}