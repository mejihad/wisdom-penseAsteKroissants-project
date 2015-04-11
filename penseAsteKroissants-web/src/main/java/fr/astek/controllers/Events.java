package fr.astek.controllers;

import fr.astek.api.services.PaKCRUDService;
import fr.astek.pac.models.Event;
import fr.astek.pac.models.UpcomingEvent;
import fr.astek.services.UpcomingEventService;
import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.annotations.scheduler.Every;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.scheduler.Scheduled;
import org.wisdom.api.templates.Template;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * Created by jmejdoub on 06/04/2015.
 */

@Controller
public class Events extends DefaultController  implements Scheduled {

    @View("welcome")
    Template welcome;

    @Requires(from = "fr.astek.services.impl.UpcomingEventJongoServiceImpl-0")
    private UpcomingEventService upComingEventJongoService;

    @Requires(from = "fr.astek.services.impl.EventJongoServiceImpl-0")
    private PaKCRUDService eventJongoService;

    @Route(method = HttpMethod.GET, uri = "/")
    public Result welcome() {
        List<UpcomingEvent> upcomingEvents = upComingEventJongoService.findAll();
        return ok(render(welcome, "welcome", "Welcome to PenseAsteKroissants!", "upcomingEvents", upcomingEvents));
    }

    @Every("4h")
    public void createNewEvent() {
        UpcomingEvent upcomingEvt = upComingEventJongoService.findFirst();
        Event event = new Event();
        event.setOrganizer(upcomingEvt.getOrganizer());
        event.setIsActive(true);
        LocalDate date = LocalDate.now();
        event.setDate(date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));
        eventJongoService.save(event);
    }
}
