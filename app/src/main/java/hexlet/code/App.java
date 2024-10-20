package hexlet.code;


import io.javalin.Javalin;

import java.io.IOException;

public class App {
    public static Javalin getApp() {

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World"));

        return app;
    }

    public static void main(String[] args) throws IOException {
        Javalin app = getApp();
        app.start(7000);
    }
}
