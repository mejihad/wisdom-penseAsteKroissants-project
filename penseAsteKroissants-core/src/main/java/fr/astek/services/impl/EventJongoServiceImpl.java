package fr.astek.services.impl;

import com.google.common.collect.Lists;
import fr.astek.api.services.PaKCRUDService;
import fr.astek.pac.models.Event;
import fr.astek.pac.models.UpcomingEvent;
import fr.astek.providers.JongoProvider;
import fr.astek.services.UpcomingEventService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.util.List;

/**
 * Created by jmejdoub on 04/04/2015.
 */
@Component
@Provides
@Instantiate
public class EventJongoServiceImpl implements UpcomingEventService<Event> {

    @Requires
    private JongoProvider jongoProvider;

    @Override
    public List<Event> findAll() {
        MongoCollection events = jongoProvider.getJongo().getCollection("events");
        MongoCursor<Event> eventsMongoCursor = events.find().as(Event.class);
        return Lists.newArrayList(eventsMongoCursor.iterator());
    }

    @Override
    public Event findById(String id) {
        return null;
    }

    @Override
    public void remove(Event evt) {

    }

    @Override
    public void save(Event evt) {
        MongoCollection events = jongoProvider.getJongo().getCollection("events");
        events.save(evt);
    }

    @Override
    public Event findFirst() {
        MongoCollection events = jongoProvider.getJongo().getCollection("events");
        Event currentEvent = events.findOne("{isActive:#}", true).as(Event.class);
        return currentEvent;
    }
}
