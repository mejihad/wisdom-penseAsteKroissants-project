package fr.astek.controllers;

import fr.astek.pac.models.Astekian;
import fr.astek.api.services.AstekianCRUDService;
import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.*;
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

    @View("welcome")
    Template welcome;

    @Requires
    private AstekianCRUDService astekianJongoService;

    @Route(method = HttpMethod.GET, uri = "/")
    public Result welcome() {
        return ok(render(welcome, "welcome", "Welcome to penseAsteKroissants!"));
    }

    @Route(method = HttpMethod.GET, uri = "/astekians")
    public Result astekiansList() {
        List<Astekian> astekians = astekianJongoService.findAll();
        return ok(render(astekiansList, "astekians", astekians));
    }

    @Route(method = HttpMethod.GET, uri = "/new")
    public Result newAstekian() {
        return ok(render(astekiansForm));
    }

    @Route(method = HttpMethod.GET, uri = "/detail/{id}")
    public Result astekianDetail(@Parameter("id") String id) {
        final Astekian astekian = (Astekian) astekianJongoService.findById(id);
        if (astekian == null) {
            return notFound(String.format("Astekian id %s does not exist !", id));
        }
        return ok(render(astekiansForm, "astekian", astekian));
    }

    @Route(method = HttpMethod.POST, uri = "/save")
    public Result save(@Body Astekian astekian) {
        astekianJongoService.save(astekian);
        return redirect("/astekians");
    }

    @Route(method = HttpMethod.DELETE, uri = "/delete/{id}")
    public Result delete(@Parameter("id") String id) {
        final Astekian astekian = (Astekian) astekianJongoService.findById(id);
        if (astekian == null) {
            return notFound(String.format("Astekian id %s does not exist !", id));
        }
        astekianJongoService.remove(astekian);
        return redirect("/astekians");
    }

}
