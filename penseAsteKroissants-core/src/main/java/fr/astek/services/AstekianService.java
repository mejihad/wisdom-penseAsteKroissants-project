package fr.astek.services;

import fr.astek.pac.models.Astekian;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

/**
 * Created by jmejdoub on 26/02/2015.
 */
@Component
@Provides
@Instantiate
public class AstekianService {

    protected long order;

    public  MongoCursor<Astekian> findAll(Jongo jongo) {
        MongoCollection astekians = jongo.getCollection("astekians");
        return astekians.find().as(Astekian.class);
    }

    public  Astekian findById(Jongo jongo, String id) {
        MongoCollection astekians = jongo.getCollection("astekians");
        return astekians.findOne("{_id:#}", new org.bson.types.ObjectId(id)).as(Astekian.class);
    }

    public  void remove(Jongo jongo, Astekian ovni) {
        MongoCollection astekians = jongo.getCollection("astekians");
        astekians.remove(new org.bson.types.ObjectId(ovni.id));
    }

    public void save(Jongo jongo, boolean isUpdate) {
        MongoCollection astekians = jongo.getCollection("astekians");
        if (!isUpdate) {
            order = astekians.count() + 1;
        }
        astekians.save(this);
    }
}
