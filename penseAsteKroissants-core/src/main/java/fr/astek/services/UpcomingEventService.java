package fr.astek.services;

import fr.astek.api.services.PaKCRUDService;
import fr.astek.api.utils.AbstractEntity;
import fr.astek.pac.models.Event;
import fr.astek.pac.models.UpcomingEvent;

import java.util.List;

/**
 * Created by jmejdoub on 20/03/2015.
 */
public interface UpcomingEventService<T extends AbstractEntity> extends PaKCRUDService<T> {
    UpcomingEvent findFirst();
}

