package com.adwayth.eventmanagement.scheduler;

import com.adwayth.eventmanagement.entity.Event;
import com.adwayth.eventmanagement.entity.Notification;
import com.adwayth.eventmanagement.entity.Registration;
import com.adwayth.eventmanagement.entity.Student;

import com.adwayth.eventmanagement.repository.EventRepository;
import com.adwayth.eventmanagement.repository.NotificationRepository;
import com.adwayth.eventmanagement.repository.RegistrationRepository;
import com.adwayth.eventmanagement.repository.StudentRepository;

import com.adwayth.eventmanagement.service.EmailService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class EventNotificationScheduler {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public EventNotificationScheduler(
            EventRepository eventRepository,
            RegistrationRepository registrationRepository,
            StudentRepository studentRepository,
            NotificationRepository notificationRepository,
            EmailService emailService) {

        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkEvents() {

        LocalDateTime now = LocalDateTime.now();

        List<Event> events = eventRepository.findAll();

        for (Event event : events) {

            if (event.getStartDateTime() != null
                    && !event.isStartNotificationSent()
                    && !now.isBefore(event.getStartDateTime())) {

                sendStartNotifications(event);

                event.setStartNotificationSent(true);
                eventRepository.save(event);
            }

            if (event.getEndDateTime() != null
                    && !event.isEndNotificationSent()
                    && !now.isBefore(event.getEndDateTime())) {

                sendEndNotifications(event);

                event.setEndNotificationSent(true);
                eventRepository.save(event);
            }
        }
    }

    private void sendStartNotifications(Event event) {

        List<Registration> registrations =
                registrationRepository
                        .findByEventId(event.getEventId());

        Set<Integer> studentIds = new HashSet<>();

        for (Registration registration : registrations) {
            studentIds.add(registration.getStudentId());
        }

        for (Integer studentId : studentIds) {

            Student student =
                    studentRepository
                            .findById(studentId)
                            .orElse(null);

            if (student == null) {
                continue;
            }

            Notification notification = new Notification();

            notification.setStudentId(studentId);

            notification.setMessage(
                    "Your event \"" +
                    event.getEventName() +
                    "\" has started."
            );

            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);

            if (student.getEmail() != null
                    && !student.getEmail().isEmpty()) {

                try {
                    emailService.sendEventStartedEmail(
                            student.getEmail(),
                            event.getEventName(),
                            event.getVenue()
                    );

                } catch (Exception e) {
                    System.out.println(
                        "Failed to send start email to "
                        + student.getEmail()
                    );
                }
            }
        }
    }

    private void sendEndNotifications(Event event) {

        List<Registration> registrations =
                registrationRepository
                        .findByEventId(event.getEventId());

        Set<Integer> studentIds = new HashSet<>();

        for (Registration registration : registrations) {
            studentIds.add(registration.getStudentId());
        }

        for (Integer studentId : studentIds) {

            Student student =
                    studentRepository
                            .findById(studentId)
                            .orElse(null);

            if (student == null) {
                continue;
            }

            Notification notification = new Notification();

            notification.setStudentId(studentId);

            notification.setMessage(
                    "Your event \"" +
                    event.getEventName() +
                    "\" has ended."
            );

            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);

            if (student.getEmail() != null
                    && !student.getEmail().isEmpty()) {

                try {
                    emailService.sendEventEndedEmail(
                            student.getEmail(),
                            event.getEventName()
                    );

                } catch (Exception e) {
                    System.out.println(
                        "Failed to send end email to "
                        + student.getEmail()
                    );
                }
            }
        }
    }
}