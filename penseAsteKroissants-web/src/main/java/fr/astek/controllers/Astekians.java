package fr.astek.controllers;

import fr.astek.pac.models.Astekian;
import fr.astek.api.services.AstekianCRUDService;
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
 * Created by jmejdoub on 26/02/2015.
 */
@Controller
public class Astekians extends DefaultController {

    @View("astekiansList")
    Template astekiansList;

    @View("astekiansForm")
    Template astekiansForm;

    @Requires
    private AstekianCRUDService astekianJongoService;

    @Route(method = HttpMethod.GET, uri = "/astekiansList")
    public Result astekiansList() {
        List<Astekian> astekians = astekianJongoService.findAll();
        return ok(render(astekiansList, "astekians", astekians));
    }

    @Route(method = HttpMethod.GET, uri = "/new")
    public Result newAstekian() {
        return ok(render(astekiansForm));
    }
}
