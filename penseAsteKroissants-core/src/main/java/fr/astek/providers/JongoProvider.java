package fr.astek.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mongodb.*;
import fr.astek.pac.dtos.Organizer;
import fr.astek.pac.models.Astekian;
import fr.astek.pac.models.Event;
import fr.astek.pac.models.UpcomingEvent;
import org.apache.commons.lang3.*;
import org.apache.felix.ipojo.annotations.*;
import org.apache.felix.ipojo.annotations.Validate;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.configuration.ApplicationConfiguration;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jmejdoub on 24/02/2015.
 */

@Component
@Provides
@Instantiate
public class JongoProvider {

    final static Logger logger = LoggerFactory.getLogger(JongoProvider.class);
    @Requires
    ApplicationConfiguration configuration;

    private MongoClient mongo;
    private Jongo jongo;
    private static final String MONGOHOST = "mongodb.host";
    private static final String MONGOPORT = "mongodb.port";
    private static final String UPCOMING_EVTS_SIZE = "upcoming.events.size";


    @Validate
    protected void configure() {
        String host = configuration.get(MONGOHOST);
        int port = configuration.getInteger(MONGOPORT);
        int upEvtSize = configuration.getInteger(UPCOMING_EVTS_SIZE);
                logger.info(String.format("host: %s, portport: %s",host,port));
        try {
            mongo = new MongoClient(host,port);
        } catch (UnknownHostException e) {
            logger.error("Unable to create Mongo instance.", e);
            throw new RuntimeException("Unable to create Mongo instance.", e);
        }
        DB db = mongo.getDB("penseAsteKroissants");
        jongo = new Jongo(db);
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
        /* if (!db.collectionExists("events")) {
            db.createCollection("events",null);
        }*/
       if (!db.collectionExists("upcomingEvents")) {
            BasicDBObject options = new BasicDBObject("capped", true);
            options.append("size", 4096);
            options.append("max", upEvtSize);
            db.createCollection("upcomingEvents", options);
            //Init capped collection to manage upcoming events
            MongoCollection upcomingEvents = jongo.getCollection("upcomingEvents");
            //Init users list to populate capped collection
           List<Astekian> astekiansList = Lists.newArrayList(jongo.getCollection("astekians").find().sort("{order: 1}").as(Astekian.class).iterator());
           MongoCollection events = jongo.getCollection("events");
           Event currentEvent = events.findOne("{isActive:#}", true).as(Event.class);
           List<Astekian> subList = new ArrayList<Astekian>();
           if(currentEvent != null) {
               final String currentOrganizerId = currentEvent.getOrganizer().getAstekianId();
               Optional<Astekian> opAstekian = astekiansList
                       .stream()
                       .filter(a -> StringUtils.equals(a.getId(),currentOrganizerId))
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
               UpcomingEvent upcomingEvent = new UpcomingEvent();
               Organizer org = new Organizer(organizer.getId(),organizer.getFirstName(),organizer.getLastName(),null);
               upcomingEvent.setOrganizer(org);
               upcomingEvents.save(upcomingEvent);
           }
        }


    }

    public Jongo getJongo() {
        return jongo;
    }

}
