package fr.astek.api.services;

import fr.astek.api.utils.AbstractEntity;
import java.util.List;

/**
 * Created by jmejdoub on 20/03/2015.
 */
public interface PaKCRUDService<T extends AbstractEntity> {

    public List<T> findAll();

    public T findById(String id);

    public void remove(T entity);

    public void save(T entity);
}

