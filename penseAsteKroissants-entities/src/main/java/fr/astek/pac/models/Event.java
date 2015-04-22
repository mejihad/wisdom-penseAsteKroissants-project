package fr.astek.pac.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import fr.astek.pac.dtos.Organizer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by jmejdoub on 04/04/2015.
 */
public class Event extends UpcomingEvent{

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    protected LocalDate date;

    protected boolean isActive;

    public Event() {
    }

    public Event(Organizer organizer, LocalDate date, boolean isActive) {
        super(organizer);
        this.date = date;
        this.isActive = isActive;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getDateStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }
}
