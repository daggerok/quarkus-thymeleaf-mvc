package com.github.daggerok.quarkus.resteasyhtmlthymeleaf;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.jboss.resteasy.plugins.providers.html.Renderable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Log
@ApplicationScoped
public class Rendering {

    @Produces
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
        return templateEngine;
    }

    public ThymeleafView view(String relativePath) {
        String templatePath = String.format("templates/%s.html", relativePath);
        return new ThymeleafView(templatePath, templateEngine());
    }

    @RequiredArgsConstructor
    public static class ThymeleafView implements Renderable {

        private final String path;
        private final TemplateEngine templateEngine;
        private final Map<String, Object> variables = new HashMap<>();

        public ThymeleafView with(String key, Object variable) {
            this.variables.put(key, variable);
            return this;
        }

        @Override
        public void render(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException, WebApplicationException {

            WebContext context = new WebContext(request, response, request.getServletContext());
            context.setVariables(variables);

            try (ServletOutputStream outputStream = response.getOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                templateEngine.process(path, context, writer);
            }
        }
    }
}
