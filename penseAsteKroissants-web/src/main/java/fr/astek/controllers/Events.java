package fr.astek.controllers;

import fr.astek.pac.models.Astekian;
import fr.astek.pac.models.Event;
import fr.astek.pac.models.UpcomingEvent;
import fr.astek.services.UpcomingEventService;
import fr.astek.services.impl.EventJongoServiceImpl;
import fr.astek.services.impl.EventsManager;
import fr.astek.services.impl.MailService;
import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.annotations.scheduler.Every;
import org.wisdom.api.configuration.ApplicationConfiguration;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.scheduler.Scheduled;
import org.wisdom.api.templates.Template;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmejdoub on 06/04/2015.
 */

@Controller
public class Events extends DefaultController  implements Scheduled {

    @View("welcome")
    Template welcome;

    @Requires(from = "fr.astek.services.impl.UpcomingEventJongoServiceImpl-0")
    private UpcomingEventService upComingEventJongoService;

    @Requires
    EventsManager eventsManager;

    @Requires
    MailService mailer;

    @Requires(from = "fr.astek.services.impl.EventJongoServiceImpl-0")
    private UpcomingEventService eventJongoService;

    @Route(method = HttpMethod.GET, uri = "/")
    public Result welcome() {
        List<UpcomingEvent> upcomingEvents = upComingEventJongoService.findAll();
        Event currentEvent = (Event) eventJongoService.findFirst();
        return ok(render(welcome, "welcome", "Welcome to PenseAsteKroissants!", "upcomingEvents", upcomingEvents, "currentEvent", currentEvent));
    }

    @Every("1m")
    public void createNewEvent() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        if(DayOfWeek.MONDAY.equals(now.getDayOfWeek()) && now.getHour() >= 9){
            eventsManager.createEvent();
        }else if(DayOfWeek.THURSDAY.equals(now.getDayOfWeek()) && now.getHour() >= 9) {
            eventsManager.remindEvent();
        }
    }
}
