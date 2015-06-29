package fr.astek.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import fr.astek.pac.dtos.Organizer;
import fr.astek.pac.models.Astekian;
import fr.astek.pac.models.Event;
import fr.astek.pac.models.UpcomingEvent;
import fr.astek.providers.JongoProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.ipojo.annotations.*;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.annotations.View;
import org.wisdom.api.configuration.ApplicationConfiguration;
import org.wisdom.api.templates.Template;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Created by jmejdoub on 07/04/2015.
 */
@Component
@Provides
@Instantiate
public class EventsManager {

    final static Logger logger = LoggerFactory.getLogger(EventsManager.class);

    @View("mail/email")
    Template email;

    @Requires
    JongoProvider jongoProvider;

    @Requires
    ApplicationConfiguration configuration;

    @Requires
    MailService mailer;

    private static final String UPCOMING_EVTS_SIZE = "upcoming.events.size";

    private static final String UPCOMING_EVTS_MAIL_SUBJECT = "mail.pak.subject";

    private static final String EVT_TO_REMIND_MAIL_SUBJECT = "mail.pak.reminder.subject";

    @Validate
    protected void configure() throws Exception {
        int upEvtSize = configuration.getInteger(UPCOMING_EVTS_SIZE);

        Jongo jongo = jongoProvider.getJongo();
        DB db = jongo.getDatabase();
        if (!db.collectionExists("astekians")) {
            ClassLoader classLoader = getClass().getClassLoader();
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<Astekian> ovnis = mapper.readValue(classLoader.getResource("astekians.json"), new TypeReference<List<Astekian>>() { });
                MongoCollection astekians = jongo.getCollection("astekians");
                for (Astekian ovni : ovnis) {
                    astekians.save(ovni);
                }
            } catch (IOException e) {
                logger.error("Error occured while reading astekians.json file", e);
                throw new RuntimeException("Error occured while reading astekians.json file", e);
            }
        }

        if (!db.collectionExists("upcomingEvents")) {
            initUpcomingEvents(jongo, upEvtSize);
        }

        createEvent();
    }

    public void initUpcomingEvents(Jongo jongo, int upEvtSize) throws Exception {
        logger.info("Start upcoming events initialization.");
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

    public void createEvent() throws Exception {
        Jongo jongo = jongoProvider.getJongo();
        MongoCollection events = jongo.getCollection("events");
        Event currentEvent = events.findOne("{isActive:#}", true).as(Event.class);
        Event event = new Event();
        LocalDate nextFriday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        if(currentEvent == null || !currentEvent.getDate().equals(nextFriday)){
            logger.info("Start Event creation.");
            MongoCollection upcomingEvents = jongo.getCollection("upcomingEvents");
            //First upcoming event (natural sort)
            UpcomingEvent upcomingEvt = upcomingEvents.findOne().as(UpcomingEvent.class);
            final Organizer organizer = upcomingEvt.getOrganizer();
            event.setOrganizer(organizer);
            event.setIsActive(true);
            event.setToRemind(true);
            LocalDate date = LocalDate.now();
            event.setDate(date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));
            events.save(event);
            Map<String, Object> params = new HashMap();
            params.put("astekian", organizer);
            String email = configuration.isDev() ? "mejihad@gmail.com" : organizer.getEmail();
            mailer.sendMail(this.email, params, email, configuration.get(UPCOMING_EVTS_MAIL_SUBJECT));
            if (currentEvent != null) {
                currentEvent.setIsActive(false);
                events.save(currentEvent);
            }
            //Last upcoming event
            UpcomingEvent lastUpcomingEvt = upcomingEvents.findOne().orderBy("{$natural: -1}").as(UpcomingEvent.class);
            MongoCollection astekians = jongo.getCollection("astekians");
            long lastOrgOrder = astekians.findOne("{_id:#}", new org.bson.types.ObjectId(lastUpcomingEvt.getOrganizer().getAstekianId())).as(Astekian.class).getOrder();
            Astekian nextOrganizer = null;
            if(astekians.count() != lastOrgOrder){
                nextOrganizer = astekians.findOne("{isInMission:#, order: {$gt: #}}", false, lastOrgOrder).orderBy("{order: 1}").as(Astekian.class);
            }else {
                nextOrganizer = astekians.findOne("{isInMission:#}", false).orderBy("{order: 1}").as(Astekian.class);
            }
            createUpcomingEvent(upcomingEvents, nextOrganizer);
        }
    }

    public void remindEvent() throws Exception {
        Jongo jongo = jongoProvider.getJongo();
        MongoCollection events = jongo.getCollection("events");
        Event eventToRemind = events.findOne("{toRemind:#}", true).as(Event.class);
        if(eventToRemind == null){
            return;
        }
        Map<String, Object> params = new HashMap();
        params.put("astekian", eventToRemind.getOrganizer());

        mailer.sendMail(email, params, eventToRemind.getOrganizer().getEmail(), configuration.get(EVT_TO_REMIND_MAIL_SUBJECT));
        eventToRemind.setToRemind(false);
        events.save(eventToRemind);
    }

    private void createUpcomingEvent(MongoCollection upcomingEvents, Astekian organizer) throws Exception {
        UpcomingEvent upcomingEvent = new UpcomingEvent();
        Organizer org = new Organizer(organizer.getId(),organizer.getFirstName(),organizer.getLastName(),organizer.getEmail());
        upcomingEvent.setOrganizer(org);
        upcomingEvents.save(upcomingEvent);
    }
}
