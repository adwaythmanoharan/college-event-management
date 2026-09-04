package com.adwayth.eventmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name="registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int registrationId;

    private int studentId;

    private int eventId;

    private int subEventId;

    public Registration()
    {

    }

    public int getRegistrationId()
    {
        return registrationId;
    }

    public void setRegistrationId(int registrationId)
    {
        this.registrationId=registrationId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getSubEventId() {
    return subEventId;
}

    public void setSubEventId(int subEventId) {
    this.subEventId = subEventId;
}
}
