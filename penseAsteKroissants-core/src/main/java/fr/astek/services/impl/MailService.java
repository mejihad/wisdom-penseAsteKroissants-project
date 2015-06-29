package fr.astek.services.impl;

import fr.astek.api.utils.PakFakeContext;
import fr.astek.pac.models.Astekian;
import org.apache.commons.io.Charsets;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.framework.BundleContext;
import org.ow2.chameleon.mail.Mail;
import org.ow2.chameleon.mail.MailSenderService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by jmejdoub on 04/06/2015.
 */

@Component
@Provides
@Instantiate
public class MailService {

    @Requires
    MailSenderService mailer;

    public void sendMail(Template template, Map<String, Object> params, String email, String subject) throws Exception {
        org.wisdom.api.http.Context.CONTEXT.set(new PakFakeContext());
        /**/
        Renderable content = template.render(null, params);

        mailer.send(new Mail()
                .to(email)
                .subject(subject)
                .body(content.content().toString())
                .charset(Charsets.UTF_8.displayName())
                .subType("html"));
    }
    public void sendMail2(String templateName, String templateSuffix, String templatePrefix, Map<String, Object> params, String email, String subject) throws Exception {
        /**/
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML5");
        //resolver.setSuffix(".html");
        resolver.setSuffix(templateSuffix);
        //resolver.setPrefix("templates/mail");
        resolver.setPrefix(templatePrefix);
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        StringWriter writer = new StringWriter();
        Context context = new Context();
        context.setVariables(params);
        engine.process(templateName, context, writer);

        mailer.send(new Mail()
                .to(email)
                .subject(subject)
                .body(writer.toString())
                .charset(Charsets.UTF_8.displayName())
                .subType("html"));
    }
}
