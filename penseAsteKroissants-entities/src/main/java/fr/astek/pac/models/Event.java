package fr.astek.pac.models;

import fr.astek.pac.dtos.Organizer;

import java.time.LocalDate;

/**
 * Created by jmejdoub on 04/04/2015.
 */
public class Event extends UpcomingEvent{

    protected LocalDate date;

    protected boolean isActive;

    public Event() {
    }

    public Event(Organizer organizer, LocalDate date, boolean isActive) {
        super(organizer);
        this.date = date;
        this.isActive = isActive;
    }
}
