package com.adwayth.eventmanagement.repository;

import com.adwayth.eventmanagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Integer> {

    List<Notification> findByStudentIdOrderByCreatedAtDesc(int studentId);

    long countByStudentIdAndReadFalse(int studentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n " +
           "WHERE n.notificationId = :notificationId " +
           "AND n.studentId = :studentId")
    void deleteByNotificationIdAndStudentId(
            @Param("notificationId") int notificationId,
            @Param("studentId") int studentId
    );
}