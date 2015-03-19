package fr.astek.api.providers;

import com.mongodb.MongoClient;
import org.apache.felix.ipojo.annotations.*;
import org.jongo.Jongo;
import com.mongodb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.configuration.ApplicationConfiguration;

import java.net.UnknownHostException;

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
            throw new RuntimeException("Unable to create Mongo instance.", e);
        }
        DB db = mongo.getDB("penseAsteKroissants");
        jongo = new Jongo(db);
    }

    public Jongo getJongo() {
        return jongo;
    }

}
