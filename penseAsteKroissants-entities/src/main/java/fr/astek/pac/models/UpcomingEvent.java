package fr.astek.pac.models;

import fr.astek.api.utils.AbstractEntity;
import fr.astek.pac.dtos.Organizer;
import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

/**
 * Created by jmejdoub on 03/04/2015.
 */
public class UpcomingEvent extends AbstractEntity {


    @Id
    @ObjectId
    protected String id ;

    protected Organizer organizer;

    public UpcomingEvent() {
    }

    /**
     *
     * @param organizer
     */
    public UpcomingEvent(Organizer organizer) {
        this.organizer = organizer;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }
}
