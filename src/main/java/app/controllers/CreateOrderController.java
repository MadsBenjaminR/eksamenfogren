package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderlineMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @author laith kaseb
 **/

public class CreateOrderController {


    public static void addRoutes(Javalin app, ConnectionPool connectionPool){

        app.get("/custompage",ctx -> loadpage(ctx));
        app.post("/custompage",ctx -> loadpage(ctx));

        app.post("/requestreceipt",ctx-> createOrder(ctx,connectionPool));


    }


    public static void loadpage(Context ctx){
        ctx.sessionAttribute("currentLength",0);
        ctx.sessionAttribute("currentWidth",0);
        ctx.render("custompage.html");
    }


    public static void createOrder(Context ctx, ConnectionPool connectionPool) throws SQLException, DatabaseException {
        int  w = Integer.parseInt(ctx.formParam("width"));
        int  l = Integer.parseInt(ctx.formParam("length"));


        ctx.sessionAttribute("currentLength",l);
        ctx.sessionAttribute("currentWidth",w);


        User user = ctx.sessionAttribute("currentUser");
        int length= Integer.parseInt(ctx.formParam("length"));
        int width= Integer.parseInt(ctx.formParam("width"));
        LocalDate date=LocalDate.now();

        OrderlineMapper.makeAnOrder("carport","tilbud ikke sendt",date,user,width,length,connectionPool);


        ctx.render("requestreceipt.html");



    }

}
