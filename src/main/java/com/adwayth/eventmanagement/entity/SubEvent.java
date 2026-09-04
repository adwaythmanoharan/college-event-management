package com.adwayth.eventmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sub_events")
public class SubEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subEventId;

    private String subEventName;

    private int eventId;

    public SubEvent() {
    }

    public int getSubEventId() {
        return subEventId;
    }

    public void setSubEventId(int subEventId) {
        this.subEventId = subEventId;
    }

    public String getSubEventName() {
        return subEventName;
    }

    public void setSubEventName(String subEventName) {
        this.subEventName = subEventName;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}