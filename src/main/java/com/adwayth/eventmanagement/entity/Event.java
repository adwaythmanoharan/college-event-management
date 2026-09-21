package com.adwayth.eventmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    private String eventName;
    private String venue;
    private String imageUrl;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private boolean startNotificationSent;
    private boolean endNotificationSent;

    public Event() {}

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isStartNotificationSent() {
    return startNotificationSent;
}

public void setStartNotificationSent(boolean startNotificationSent) {
    this.startNotificationSent = startNotificationSent;
}

public boolean isEndNotificationSent() {
    return endNotificationSent;
}

public void setEndNotificationSent(boolean endNotificationSent) {
    this.endNotificationSent = endNotificationSent;
}
}