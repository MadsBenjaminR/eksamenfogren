package app.controllers;

import app.entities.User;
import app.persistence.*;
import app.services.*;
import app.entities.Orders;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Locale;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class OrderController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool){
        app.get("/adminrequest", ctx-> displayAllOrders(ctx, connectionPool));
        app.post("/payment", ctx -> showCarport(ctx, connectionPool));




    }



    private static void displayOffer(Context ctx, ConnectionPool connectionPool) {

        List<Orders> orderList=OrderMapper.getAllOrders(connectionPool);
        ctx.sessionAttribute("ordersList", orderList);
        ctx.render("payment.html");

    }


    public static void displayAllOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Orders> orderList=OrderMapper.getAllOrders(connectionPool);
        ctx.sessionAttribute("ordersList", orderList);
        ctx.render("adminrequest.html");
    }



    public static void showCarport(Context ctx, ConnectionPool connectionPool){


        Locale.setDefault(new Locale("US"));
        User user = ctx.sessionAttribute("currentUser");


        try {
            Orders order = OrderMapper.getCustomerDim(user.getUserId(), connectionPool);

            int customerWidth = order.getCustomerWidth();
            int customerLength = order.getCustomerLength();


            CarportSvg carportSvg = new CarportSvg(customerWidth,customerLength);

            ArrowSvg arrowSvg = new ArrowSvg(customerWidth,customerLength);
            String combinedSvg = arrowSvg.getArrowSvg2().addSvg2(carportSvg.getCarportSvg2());



            List<Orders> orderList =OrderMapper.getAllOrders(connectionPool);
            ctx.sessionAttribute("ordersList", orderList);
            ctx.attribute("svg",combinedSvg);
            ctx.render("payment.html");



        } catch (DatabaseException e) {
            //Fejlmeddelelsen fra DatabaseException tilføjes som en attribut til kontekstobjektet.
            ctx.attribute("message", e.getMessage());
            ctx.render("login.html");
        }



    }


}
