package com.adwayth.eventmanagement.controller;

import com.adwayth.eventmanagement.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/test-email")
    public String testEmail() {

        try {

            emailService.sendEventStartedEmail(
                    "manoharanadwaith@gmail.com",
                    "CampusConnect Test Event",
                    "MCA Hall"
            );

            return "EMAIL SENT SUCCESSFULLY";

        } catch (Exception e) {

            e.printStackTrace();

            return "EMAIL FAILED: " + e.getMessage();
        }
    }
}