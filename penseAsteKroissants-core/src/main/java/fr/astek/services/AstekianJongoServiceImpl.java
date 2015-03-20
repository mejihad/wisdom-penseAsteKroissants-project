package fr.astek.services;

import com.google.common.collect.Lists;
import fr.astek.api.providers.JongoProvider;
import fr.astek.api.services.AstekianCRUDService;
import fr.astek.pac.models.Astekian;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.util.List;

/**
 * Created by jmejdoub on 26/02/2015.
 */
@Component
@Provides
@Instantiate
public class AstekianJongoServiceImpl implements AstekianCRUDService {

    @Requires
    private JongoProvider jongoProvider;

    protected long order;

    public List<Astekian> findAll() {
        MongoCollection astekians = jongoProvider.getJongo().getCollection("astekians");
        MongoCursor<Astekian> astekianMongoCursor = astekians.find().as(Astekian.class);
        return Lists.newArrayList(astekianMongoCursor.iterator());
    }

    public  Astekian findById(String id) {
        MongoCollection astekians = jongoProvider.getJongo().getCollection("astekians");
        return astekians.findOne("{_id:#}", new org.bson.types.ObjectId(id)).as(Astekian.class);
    }

    public  void remove(Astekian ovni) {
        MongoCollection astekians = jongoProvider.getJongo().getCollection("astekians");
        astekians.remove(new org.bson.types.ObjectId(ovni.id));
    }

    public void save(boolean isUpdate) {
        MongoCollection astekians = jongoProvider.getJongo().getCollection("astekians");
        if (!isUpdate) {
            order = astekians.count() + 1;
        }
        astekians.save(this);
    }
}
