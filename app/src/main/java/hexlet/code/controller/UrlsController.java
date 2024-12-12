package hexlet.code.controller;

import hexlet.code.NamedRoutes;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.urls.UrlPage;
import hexlet.code.urls.UrlsPage;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void create(Context ctx) throws SQLException {
        String urlString = ctx.formParam("url").toLowerCase().trim();

        String normalizedUrlString;
        try {
            normalizedUrlString = normalizeUrlString(urlString);
        } catch (MalformedURLException | IllegalArgumentException | URISyntaxException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("alertType", "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        if (!UrlsRepository.urlExists(normalizedUrlString)) {
            Url url = new Url(normalizedUrlString);
            UrlsRepository.save(url);

            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("alertType", "success");
            ctx.redirect(NamedRoutes.urlsPath());
        } else {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("alertType", "danger");
            ctx.redirect(NamedRoutes.urlsPath());
        }
    }

    public static void show(Context ctx) throws SQLException {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        Url url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Page not found"));
        List<UrlCheck> urlChecks = UrlChecksRepository.getAllChecksForUrl(id);

        UrlPage page = new UrlPage(url);
        page.setUrlChecks(urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setAlertType(ctx.consumeSessionAttribute("alertType"));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlsRepository.getEntities();
        Map<Integer, UrlCheck> allUrlsLastChecks = UrlChecksRepository.getAllUrlsLastChecks();
        UrlsPage page = new UrlsPage(urls, allUrlsLastChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setAlertType(ctx.consumeSessionAttribute("alertType"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void createCheck(Context ctx) throws SQLException {
        int urlId = ctx.pathParamAsClass("id", Integer.class).get();
        Url url = UrlsRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        HttpResponse<String> response;
        try {
            response = Unirest.get(url.getName()).asString();
            Unirest.shutDown();
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Invalid URL");
            ctx.sessionAttribute("alertType", "danger");
            ctx.redirect(NamedRoutes.urlPath(String.valueOf(urlId)));
            return;
        }

        int statusCode = response.getStatus();
        String responseHTMLBody = response.getBody();

        UrlCheck urlCheck = new UrlCheck(urlId, statusCode);

        Document document = Jsoup.parse(responseHTMLBody);
        urlCheck.setTitle(document.title());
        Element h1 = document.select("h1").first();
        urlCheck.setH1(h1 == null ? null : h1.text());
        Element content = document.select("meta[name=description]").first();
        urlCheck.setDescription(content == null ? null : content.attr("content"));
        UrlChecksRepository.save(urlCheck);

        ctx.sessionAttribute("flash", "Страница успешно проверена");
        ctx.sessionAttribute("alertType", "success");
        ctx.redirect(NamedRoutes.urlPath(String.valueOf(urlId)));
    }
    public static String normalizeUrlString(String urlString) throws MalformedURLException,
            IllegalArgumentException,
            URISyntaxException {
        URI inputUri = new URI(urlString);
        URL inputUrl = inputUri.toURL();
        return String.format("%s://%s%s",
                inputUrl.getProtocol(),
                inputUrl.getHost(),
                (inputUrl.getPort() == -1 ? "" : ":" + inputUrl.getPort()));
    }
}
