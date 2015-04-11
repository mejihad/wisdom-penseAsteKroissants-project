package fr.astek.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mongodb.*;
import fr.astek.pac.dtos.Organizer;
import fr.astek.pac.models.Astekian;
import fr.astek.pac.models.Event;
import fr.astek.pac.models.UpcomingEvent;
import fr.astek.services.impl.EventsManager;
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
    @Requires
    EventsManager eventsManager;

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

       if (!db.collectionExists("upcomingEvents")) {
           eventsManager.initUpcomingEvents(jongo, upEvtSize);
        }

        eventsManager.createEvent(jongo);
    }

    public Jongo getJongo() {
        return jongo;
    }

}
