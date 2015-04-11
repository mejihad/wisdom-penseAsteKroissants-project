package fr.astek.services.impl;

import com.google.common.collect.Lists;
import fr.astek.api.utils.AbstractEntity;
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
public class UpcomingEventJongoServiceImpl implements UpcomingEventService<UpcomingEvent> {

    @Requires
    private JongoProvider jongoProvider;

    @Override
    public List<UpcomingEvent> findAll() {
        MongoCollection upcomingEvents = jongoProvider.getJongo().getCollection("upcomingEvents");
        MongoCursor<UpcomingEvent> upcomingEventsMongoCursor = upcomingEvents.find().as(UpcomingEvent.class);
        return Lists.newArrayList(upcomingEventsMongoCursor.iterator());
    }

    @Override
    public UpcomingEvent findById(String id) {
        return null;
    }

    @Override
    public UpcomingEvent findFirst() {
        MongoCollection upcomingEvents = jongoProvider.getJongo().getCollection("upcomingEvents");
        return upcomingEvents.findOne().as(UpcomingEvent.class);
    }

    @Override
    public void remove(UpcomingEvent entity) {

    }

    @Override
    public void save(UpcomingEvent upcomingEvt) {
        MongoCollection upcomingEvents = jongoProvider.getJongo().getCollection("upcomingEvents");
        upcomingEvents.save(upcomingEvt);
    }
}
