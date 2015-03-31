package fr.astek.providers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import fr.astek.api.services.AstekianCRUDService;
import fr.astek.pac.models.Astekian;
import org.apache.felix.ipojo.annotations.*;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.configuration.ApplicationConfiguration;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

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


    @Validate
    protected void configure() {
        String host = configuration.get(MONGOHOST);
        int port = configuration.getInteger(MONGOPORT);
        logger.info(String.format("host: %s, port: %s",host,port));
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
        /*if (!db.collectionExists("upComingEvents")) {
            DBObject options = BasicDBObjectBuilder.start().add("capped", true).add("max", 20).get();
            db.createCollection("upComingEvents", options);
            //Init capped collection to manage upcoming events

            //JSON.pars
        }*/


    }

    public Jongo getJongo() {
        return jongo;
    }

}
