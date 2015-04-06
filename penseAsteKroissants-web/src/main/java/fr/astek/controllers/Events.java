package fr.astek.controllers;

import fr.astek.api.services.PaKCRUDService;
import fr.astek.pac.models.UpcomingEvent;
import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

import java.util.List;

/**
 * Created by jmejdoub on 06/04/2015.
 */

@Controller
public class Events extends DefaultController {

    @View("welcome")
    Template welcome;

    @Requires(from = "fr.astek.services.UpcomingEventJongoServiceImpl-0")
    private PaKCRUDService upComingEventJongoService;

    @Route(method = HttpMethod.GET, uri = "/")
    public Result welcome() {
        List<UpcomingEvent> upcomingEvents = upComingEventJongoService.findAll();
        return ok(render(welcome, "welcome", "Welcome to penseAsteKroissants!", "upcomingEvents", upcomingEvents));
    }
}
