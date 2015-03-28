package fr.astek.services;

import com.google.common.collect.Lists;
import fr.astek.api.providers.JongoProvider;
import fr.astek.api.services.AstekianCRUDService;
import fr.astek.pac.models.Astekian;
import org.apache.commons.lang3.StringUtils;
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
        long order = ovni.getOrder();
        astekians.remove(new org.bson.types.ObjectId(ovni.getId()));
        astekians.update("{order: {$gt: #}}",order).multi().with("{$inc: {order: -1}}");
    }

    public void save(Astekian ovni) {
        MongoCollection astekians = jongoProvider.getJongo().getCollection("astekians");
        if (StringUtils.isBlank(ovni.getId())) {
            ovni.setId(null);
            long order = astekians.count() + 1;
            ovni.setOrder(order);
        }
        astekians.save(ovni);
    }
}
