package fr.astek.api.services;

import fr.astek.pac.models.Astekian;
import java.util.List;

/**
 * Created by jmejdoub on 20/03/2015.
 */
public interface AstekianCRUDService {

    public List<Astekian> findAll();

    public  Astekian findById(String id);

    public  void remove(Astekian ovni);

    public void save(Astekian ovni);
}

