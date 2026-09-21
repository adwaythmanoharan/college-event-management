package com.adwayth.eventmanagement.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    private final HttpClient client =
            HttpClient.newHttpClient();

    private final String fromEmail =
            "CampusConnect <onboarding@resend.dev>";

    public void sendEventStartedEmail(
            String to,
            String eventName,
            String venue) {

        String body =
                "Hello,\n\n" +
                "Your registered event has started.\n\n" +
                "Event: " + eventName + "\n" +
                "Venue: " + venue + "\n\n" +
                "Enjoy the event!\n\n" +
                "CampusConnect";

        sendEmail(
                to,
                "CampusConnect - Event Started",
                body
        );
    }

    public void sendEventEndedEmail(
            String to,
            String eventName) {

        String body =
                "Hello,\n\n" +
                "Your registered event has ended.\n\n" +
                "Event: " + eventName + "\n\n" +
                "Thank you for participating.\n\n" +
                "CampusConnect";

        sendEmail(
                to,
                "CampusConnect - Event Ended",
                body
        );
    }

    private void sendEmail(
            String to,
            String subject,
            String body) {

        String json =
                "{"
                + "\"from\":\"" + fromEmail + "\","
                + "\"to\":[\"" + to + "\"],"
                + "\"subject\":\"" + subject + "\","
                + "\"text\":\"" + body.replace("\n", "\\n") + "\""
                + "}";

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(
                                "https://api.resend.com/emails"))
                        .header(
                                "Authorization",
                                "Bearer " + apiKey)
                        .header(
                                "Content-Type",
                                "application/json")
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(json))
                        .build();

        try {

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString());

            System.out.println(
                    "Resend response: "
                    + response.statusCode());

            System.out.println(response.body());

            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {

                throw new RuntimeException(
                        "Email failed: "
                        + response.body());
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Could not send email",
                    e);
        }
    }
}