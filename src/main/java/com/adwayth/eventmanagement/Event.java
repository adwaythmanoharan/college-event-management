package com.adwayth.eventmanagement;

import jakarta.persistence.*;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventid;

    private String eventName;
    private String venue;

    public Event()
    {

    }
    public int getEventId(){
        return eventid;
    }

    public String getEventName(){
        return eventName;
    }

    public String getVenue(){
        return venue;
    }

    public void setEventId(int eventId){
        this.eventid= eventId;
    }

    public void setEventName(String eventName){
        this.eventName= eventName;
    }

    public void setVenue(String venue){
        this.venue= venue;
    }


}
