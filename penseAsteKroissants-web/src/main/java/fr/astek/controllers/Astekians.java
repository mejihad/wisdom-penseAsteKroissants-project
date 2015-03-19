package fr.astek.controllers;

import com.google.common.collect.Lists;
import fr.astek.pac.models.Astekian;
import fr.astek.api.providers.JongoProvider;
import fr.astek.services.AstekianService;
import org.apache.felix.ipojo.annotations.Requires;
import org.jongo.MongoCursor;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

/**
 * Created by jmejdoub on 26/02/2015.
 */
@Controller
public class Astekians extends DefaultController {

    @View("astekiansList")
    Template astekiansList;

    @Requires
    private AstekianService astekianService;

    @Requires
    private JongoProvider jongoProvider;

    @Route(method = HttpMethod.GET, uri = "/astekiansList")
    public Result astekiansList() {
        MongoCursor<Astekian> astekians =astekianService.findAll(jongoProvider.getJongo());
        return ok(render(astekiansList, "astekians", Lists.newArrayList(astekians.iterator())));
    }
}
