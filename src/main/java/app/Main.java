package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.*;
import app.entities.Orders;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    private static final String USER = System.getenv("JDBC_USER");
    private static final String PASSWORD =  System.getenv("JDBC_PASSWORD");
    private static final String URL = System.getenv("JDBC_CONNECTION_STRING");
    private static final String DB = System.getenv("JDBC_DB");
    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);



        app.get("/", ctx -> ctx.render("index.html"));
        CreateOrderController.addRoutes(app,connectionPool);
        UserController.addRoutes(app,connectionPool);
        OrderlineController.addRoutes(app,connectionPool);
        OrderController.addRoutes(app,connectionPool);
        MaterialController.addRoutes(app,connectionPool);


    }

}