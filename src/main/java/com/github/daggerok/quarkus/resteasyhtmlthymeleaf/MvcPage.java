package com.github.daggerok.quarkus.resteasyhtmlthymeleaf;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.jboss.resteasy.plugins.providers.html.Renderable;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Optional;

import static io.vavr.Predicates.not;
// import java.util.function.Predicate;

@Log
@Path("")
@Produces(MediaType.TEXT_HTML)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MvcPage {

    private final Rendering render;

    @GET
    @Path("")
    public Renderable query(@QueryParam("name") @DefaultValue("Buddy") String name) {
        log.info(() -> "query: ?name=" + name);
        return render.view("index")
                     .with("name", getOr(name, "Friend"))
                     .with("now", LocalDateTime.now());
    }

    @GET
    @Path("{path}") // @Path("{path: .*}")
    public Renderable path(@PathParam("path") String path) {
        log.info(() -> "path: '" + path + "'");
        return render.view("index")
                     .with("name", getOr(path, "Dude"))
                     .with("now", LocalDateTime.now());
    }

    private String getOr(String name, String elseValue) {
        return Optional.ofNullable(name)
                       .map(String::trim)
                       .filter(not(String::isEmpty))
                       .orElse(elseValue);
    }
}
