package app.controllers;

import app.entities.Orderline;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.OrderlineMapper;
import app.services.PartsListCalc;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class OrderlineController {



    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {

        //  app.get("/offercalculator.html", ctx -> ctx.render("offercalculator.html"));
        app.post("/calculatebutton", ctx -> displayOrderLine(ctx, connectionPool));

        app.post("/sendoffer",ctx->sendOffer(ctx,connectionPool));

        app.post("/purchasereceipt",ctx -> displayReceipt(ctx,connectionPool));


    }

    private static void displayReceipt(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        List<Orderline> orderlines = OrderlineMapper.getPartsListByOrderId(orderId, connectionPool);
        ctx.sessionAttribute("orderlines", orderlines);

        ctx.render("purchasereceipt.html");

    }

    private static void sendOffer(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        PartsListCalc.updatePriceWithId(ctx,connectionPool);
    }


    public static void displayOrderLine(Context ctx, ConnectionPool connectionPool) throws DatabaseException {


        int orderId = Integer.parseInt(ctx.formParam("orderId"));


        List<Orderline> orderlines = OrderlineMapper.getPartsListByOrderId(orderId, connectionPool);
        ctx.sessionAttribute("orderlines", orderlines);
        PartsListCalc.priceCalc(ctx,connectionPool);
        ctx.render("offercalculator.html");


    }
}