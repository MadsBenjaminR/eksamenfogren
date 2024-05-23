
package app.controllers;

import app.entities.Orders;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import app.services.PartsListCalc;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {

        app.post("/createuser", ctx -> loadCreatePage(ctx, connectionPool));
        app.post("/signup", ctx -> createUser(ctx, connectionPool));

        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> login(ctx, connectionPool));

        app.get("/index", ctx -> ctx.render("login.html"));

        app.get("/logout", ctx -> logout(ctx));


        app.get("/createuser", ctx -> ctx.render("createuser.html"));


        app.get("offeroverview.html", ctx -> offerReview(ctx, connectionPool));

        app.get("/returnlogo", ctx -> ctx.render("index.html"));

        app.get("logout", ctx -> logout(ctx));


    }

    private static void offerReview(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        User user = ctx.sessionAttribute("currentUser");
        List<Orders> orders = OrderMapper.getOrderFromUserId(user.getUserId(), connectionPool);
        ctx.sessionAttribute("orders", orders);
        PartsListCalc.calcCarport(ctx, connectionPool);


        ctx.render("offeroverview.html");
    }

    public static void loadCreatePage(Context ctx, ConnectionPool connectionPool) {
        int w = Integer.parseInt(ctx.formParam("width"));
        int l = Integer.parseInt(ctx.formParam("length"));
        ctx.sessionAttribute("currentLength", l);
        ctx.sessionAttribute("currentWidth", w);
        ctx.render("/createuser.html");

    }

    /**
     * Metoden har til formål at logge brugeren ud når det ønskes
     */

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }


    /**
     * Metoden har til formål at oprette brugeren inde på hjemmesiden sådan at man bliver sendt videre når man opretter en bruger
     */
    private static void createUser(Context ctx, ConnectionPool connectionPool) {


        //hent formparametre
        String name = ctx.formParam("name");
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        String streetName = ctx.formParam("street-name");
        int houseNumber = Integer.parseInt(ctx.formParam("house-number"));
        String floor = ctx.formParam("floor");
        int phoneNumber = Integer.parseInt(ctx.formParam("phone-number"));
        int zipCode = Integer.parseInt(ctx.formParam("zip"));

        if (password1.equals(password2)) {
            try {

                int customerId = UserMapper.createUser(name, streetName, houseNumber, floor, phoneNumber, zipCode, connectionPool);
                ctx.attribute("message", "Hermed oprettet med email: " + email + ". Log på.");
                UserMapper.createUserTable(email, password1, customerId, connectionPool);
                ctx.render("login.html");
            } catch (DatabaseException e) {
                ctx.attribute("message", "Brugernavn findes allerede - prøv igen eller log ind.");
                ctx.render("login.html");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            ctx.attribute("message", "passwords matcher ikke - prøv igen.");
            ctx.render("login.html");
        }

    }


    /**
     * Metoden skal sende brugeren videre hvis deres login er godkendt. Hvis man er admin bliver man sendt til en anden side
     */
    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String widthParam = ctx.formParam("width");
        String lengthParam = ctx.formParam("length");
        if (widthParam != null && !widthParam.isEmpty() && lengthParam != null && !lengthParam.isEmpty()) {
            try {
                int w = Integer.parseInt(ctx.formParam("width"));
                int l = Integer.parseInt(ctx.formParam("length"));
                ctx.sessionAttribute("currentLength", l);
                ctx.sessionAttribute("currentWidth", w);

            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            User user = UserMapper.login(email, password, connectionPool);
            //Hvis login-operationen lykkes, gemmes brugeren som den aktuelle bruger i sessionsattributterne i kontekstobjektet.
            ctx.sessionAttribute("currentUser", user);

            if (user.getRole().equalsIgnoreCase("customer")) {
                boolean status= OrderMapper.doesOrderByUserIdExist(user.getUserId(),connectionPool);
                if (status==true){
                    List<Orders> orders=OrderMapper.getOrderFromUserId(user.getUserId(),connectionPool);
                    ctx.sessionAttribute("orders",orders);
                    String orderStatus = orders.get(0).getStatus();
                    ctx.sessionAttribute("status", orderStatus);
                    ctx.render("offeroverview.html");
                }else {
                    ctx.render("custompage.html");
                }
            } else {
                OrderController.displayAllOrders(ctx, connectionPool);
            }
        } catch (DatabaseException e) {
            //Fejlmeddelelsen fra DatabaseException tilføjes som en attribut til kontekstobjektet.
            ctx.attribute("message", e.getMessage());
            ctx.render("login.html");
        }

    }
}
