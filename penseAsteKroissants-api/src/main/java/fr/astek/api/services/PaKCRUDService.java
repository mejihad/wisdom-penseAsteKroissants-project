package fr.astek.api.services;

import fr.astek.api.utils.AbstractEntity;
import java.util.List;

/**
 * Created by jmejdoub on 20/03/2015.
 */
public interface PaKCRUDService<T extends AbstractEntity> {

    List<T> findAll();

    T findById(String id);

    void remove(T entity);

    void save(T entity);
}

