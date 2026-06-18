package com.example.eventtrackingapp;

public class Event {
    public long id;
    public String title, date, time, location, notes;
    public boolean alertEnabled;
    public int minutesBefore;
    public String getTitle()       { return title; }
    public String getDate()        { return date; }
    public String getLocation()    { return location; }
    // Map 'description' getter to my 'notes' field:
    public String getDescription() { return notes; }


    public Event(long id, String title, String date, String time,
                 String location, String notes, boolean alertEnabled, int minutesBefore){
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.notes = notes;
        this.alertEnabled = alertEnabled;
        this.minutesBefore = minutesBefore;
    }
}