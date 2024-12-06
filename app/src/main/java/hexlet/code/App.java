package hexlet.code;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@Slf4j
public class App {
    public static Javalin getApp() throws SQLException, IOException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:project");
        var dataSource = new HikariDataSource(hikariConfig);

        // Инициализация базы данных с помощью schema.sql
        initializeDatabase(dataSource);

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get("/", ctx -> ctx.render("hello.jte"));
        app.post("/urls", ctx -> {
            String address = ctx.formParam("address");
            if (address != null && !address.isEmpty()) {
                addUrl(dataSource, address); // Обновлен метод, который использует dataSource
                ctx.result("Адрес добавлен: " + address);
            } else {
                ctx.status(400).result("Ошибка: адрес не может быть пустым.");
            }
        });
        return app;
    }

    public static void main(String[] args) throws IOException, SQLException {
        Javalin app = getApp();
        app.start(8000);
    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
    public static void addUrl(HikariDataSource dataSource, String address) {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:project");
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO urls (address) VALUES ('" + address + "');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void initializeDatabase(HikariDataSource dataSource) throws SQLException, IOException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            var sql = readResourceFile("schema.sql");
            stmt.execute(sql);
        }
    }
}
