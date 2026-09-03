package com.adwayth.eventmanagement.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name ="eventId")
    private int eventId;

    //@Column(name = "eventName")
    private String eventName;
    private String venue;

    public Event()
    {

    }
    public int getEventId(){
        return eventId;
    }

    public String getEventName(){
        return eventName;
    }

    public String getVenue(){
        return venue;
    }

    public void setEventId(int eventId){
        this.eventId= eventId;
    }

    public void setEventName(String eventName){
        this.eventName= eventName;
    }

    public void setVenue(String venue){
        this.venue= venue;
    }


}
