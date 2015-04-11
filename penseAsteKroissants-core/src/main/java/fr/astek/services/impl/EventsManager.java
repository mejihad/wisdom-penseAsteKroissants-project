package fr.astek.services.impl;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import fr.astek.pac.dtos.Organizer;
import fr.astek.pac.models.Astekian;
import fr.astek.pac.models.Event;
import fr.astek.pac.models.UpcomingEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jmejdoub on 07/04/2015.
 */
@Component
@Provides
@Instantiate
public class EventsManager {

    public void initUpcomingEvents(Jongo jongo, int upEvtSize) {
        BasicDBObject options = new BasicDBObject("capped", true);
        options.append("size", 4096);
        options.append("max", upEvtSize);
        jongo.getDatabase().createCollection("upcomingEvents", options);
        //Init capped collection to manage upcoming events
        MongoCollection upcomingEvents = jongo.getCollection("upcomingEvents");
        //Init users list to populate capped collection
        List<Astekian> astekiansList = Lists.newArrayList(jongo.getCollection("astekians").find("{isInMission:#}", false).sort("{order: 1}").as(Astekian.class).iterator());
        MongoCollection events = jongo.getCollection("events");
        Event currentEvent = events.findOne("{isActive:#}", true).as(Event.class);
        List<Astekian> subList = new ArrayList<Astekian>();
        if(currentEvent != null) {
            final String currentOrganizerId = currentEvent.getOrganizer().getAstekianId();
            Optional<Astekian> opAstekian = astekiansList
                    .stream()
                    .filter(a -> StringUtils.equals(a.getId(), currentOrganizerId))
                    .findFirst();
            int indexOfCurrOrg = astekiansList.indexOf(opAstekian.get());
            subList = astekiansList.subList(indexOfCurrOrg, astekiansList.size());
        }
        // Build list of organizers
        List<Astekian> organizersList = new ArrayList<Astekian>(subList);
        while (organizersList.size() < upEvtSize){
            organizersList.addAll(astekiansList);
        }
        for(Astekian organizer : organizersList.subList(0,upEvtSize)){
            createUpcomingEvent(upcomingEvents, organizer);
        }
    }

    public void createEvent(Jongo jongo) {
        MongoCollection events = jongo.getCollection("events");
        Event event = new Event();
        if(events.count()>0) {
            Event currentEvent = events.findOne("{isActive:#}", true).as(Event.class);
            MongoCollection upcomingEvents = jongo.getCollection("upcomingEvents");
            //Last upcoming event (natural sort)
            UpcomingEvent upcomingEvt = upcomingEvents.findOne().as(UpcomingEvent.class);
            event.setOrganizer(upcomingEvt.getOrganizer());
            event.setIsActive(true);
            LocalDate date = LocalDate.now();
            event.setDate(date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));
            events.save(event);
            currentEvent.setIsActive(false);
            events.save(currentEvent);
        }else {
            MongoCollection astekians = jongo.getCollection("astekians");
            Astekian organizer = astekians.findOne("{isInMission:#}", false).orderBy("{order: 1}").as(Astekian.class);
            event.setOrganizer(new Organizer(organizer.getId(),organizer.getFirstName(),organizer.getLastName(),null));
            event.setIsActive(true);
            LocalDate date = LocalDate.now();
            event.setDate(date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));
            events.save(event);
        }
    }

    private void createUpcomingEvent(MongoCollection upcomingEvents, Astekian organizer) {
        UpcomingEvent upcomingEvent = new UpcomingEvent();
        Organizer org = new Organizer(organizer.getId(),organizer.getFirstName(),organizer.getLastName(),null);
        upcomingEvent.setOrganizer(org);
        upcomingEvents.save(upcomingEvent);
    }
}
