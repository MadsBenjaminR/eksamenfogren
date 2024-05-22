package app.services;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.*;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laith kaseb
 **/


public class PartsListCalc {
    public static ConnectionPool connectionPool;
    private int customerLength;
    private int customerWidth;

    public PartsListCalc(int customerLength, int customerWidth) {
        this.customerLength = customerLength;
        this.customerWidth = customerWidth;
    }

    public static void calcCarport(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");

        List<Orders> ordersList = OrderMapper.getOrderFromUserId(user.getUserId(), connectionPool);

        for (Orders order: ordersList ) {

            int poleQuantity = calcPoles(order.getCustomerLength());
            int beamQuantity = calcBeamQuantity(order.getCustomerLength(), connectionPool);
            int rafterQuantity = calcRafters(order.getCustomerLength());

            int beamLength = calcBeams(order.getCustomerLength(),connectionPool);
            int rafterLength=order.getCustomerWidth();
            int poleLength=300;


            Variant beamVariant= VariantMapper.selectvariantidByLength(beamLength,connectionPool);
            Variant rafterVariant= VariantMapper.selectvariantidByLength(rafterLength,connectionPool);
            Variant poleVariant= VariantMapper.selectvariantidByLength(poleLength,connectionPool);



            OrderlineMapper.insertPartIntoOrderline(beamVariant.getVariantId(),order.getOrderId(), "Remme i sider, sadles ned i stolper",  beamQuantity,connectionPool);
            OrderlineMapper.insertPartIntoOrderline(rafterVariant.getVariantId(), order.getOrderId(), "Spær, monteres på rem",  rafterQuantity, connectionPool);
            OrderlineMapper.insertPartIntoOrderline(poleVariant.getVariantId(), order.getOrderId(), "Stolper nedgraves 90 cm. i jord",  poleQuantity, connectionPool);


        }
        ctx.render("requestreceipt");
    }

    public static void priceCalc(Context ctx, ConnectionPool connectionPool) throws DatabaseException {


        float totalPrice = partsListPriceCalcById(ctx, connectionPool);
        ctx.sessionAttribute("totalprice",totalPrice);

        float purchasePrice = partsListPurchasePriceCalcById(ctx, connectionPool);
        ctx.sessionAttribute("purchaseprice", purchasePrice);

        float profitMargin = profitMarginCalc(ctx, connectionPool);
        ctx.sessionAttribute("profitmargin", profitMargin);

    }
    public static void updatePriceWithId(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        int adminMadePrice= Integer.parseInt(ctx.formParam("set-price"));
        int orderId = Integer.parseInt(ctx.formParam("orderId"));


        OrderMapper.updateTotalPrice(adminMadePrice,orderId,connectionPool);

    }

    private static float partsListPriceCalcById(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        List<Orderline> userOrder = OrderlineMapper.getPartsListByOrderId(orderId, connectionPool);
        float totalPrice = 0;
        for (int i = 0; i <= userOrder.size()-1; i++) {

            Orderline orderline = userOrder.get(i);
            Material material = orderline.getMaterial();

            float retailPriceTotal = material.getRetailPrice();
            int quantity = orderline.getQuantity();
            int customerLengthInMeters= orderline.getVariant().getLength()/100;

            totalPrice += quantity * (retailPriceTotal * customerLengthInMeters);

        }
        return totalPrice;
    }

    private static float partsListPurchasePriceCalcById(Context ctx, ConnectionPool connectionPool){
        int orderId= Integer.parseInt(ctx.formParam("orderId"));
        List<Orderline> order = OrderlineMapper.getPartsListByOrderId(orderId, connectionPool);
        float purchasePrice = 0;
        for(int i = 0; i<order.size()-1; i++){
            Orderline orderline = order.get(i); // Fetch the current orderline
            Material material = orderline.getMaterial(); // Fetch the material associated with this orderline

            float purchasePriceTotal = material.getPurchasePrice();
            int quantity = orderline.getQuantity();
            int customerLengthInMeters = orderline.getVariant().getLength()/100;

            purchasePrice += quantity * (purchasePriceTotal * customerLengthInMeters);
        }

        return purchasePrice;
    }

    private static float partsListPriceCalc(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");
        List<Orderline> userOrder = OrderlineMapper.getPartsListPrice(user.getUserId(), connectionPool);
        float totalPrice = 0;
        for (int i = 0; i <= userOrder.size()-1; i++) {

            Orderline orderline = userOrder.get(i); // Fetch the current orderline
            Material material = orderline.getMaterial(); // Fetch the material associated with this orderline

            float retailPriceTotal = material.getRetailPrice();
            int quantity = orderline.getQuantity();
            int customerLengthInMeters= orderline.getVariant().getLength()/100;

            totalPrice += quantity * (retailPriceTotal * customerLengthInMeters); // Accumulate the line price into totalPrice


        }
        return totalPrice;
    }

    private static float partsListPurchasePriceCalc(Context ctx, ConnectionPool connectionPool){
        User user = ctx.sessionAttribute("currentUser");
        List<Orderline> order = OrderlineMapper.getPartsListPrice(user.getUserId(), connectionPool);
        float purchasePrice = 0;
        for(int i = 0; i<order.size()-1; i++){
            Orderline orderline = order.get(i); // Fetch the current orderline
            Material material = orderline.getMaterial(); // Fetch the material associated with this orderline

            float purchasePriceTotal = material.getPurchasePrice();
            int quantity = orderline.getQuantity();
            int customerLengthInMeters = orderline.getVariant().getLength()/100;

            purchasePrice += quantity * (purchasePriceTotal * customerLengthInMeters);
        }

        return purchasePrice;
    }

    private static float profitMarginCalc(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        float contributionMargin = partsListPriceCalc(ctx, connectionPool) - partsListPurchasePriceCalc(ctx, connectionPool);
        float profitMargin = contributionMargin / partsListPriceCalc(ctx, connectionPool) *100;

        return profitMargin;
    }


    public static int calcBeams(int customlength, ConnectionPool connectionPool) {
        int bestMatch = 0;

        List<Variant> variant = VariantMapper.selectVariantLengthById(5, connectionPool);
        int variantCount = variant.size();
        if (customlength > 600) {
            int length = customlength / 2;
            for (int i = 0; i < variantCount; i++) {
                int variantLength = variant.get(i).getLength();
                bestMatch = variantLength;
                if (bestMatch >= length) {
                    break;
                }
            }
        } else {
            for (int i = 0; i < variantCount; i++) {
                int variantLength = variant.get(i).getLength();
                bestMatch = variantLength;
                if (bestMatch >= customlength) {
                    break;
                }

            }

            return bestMatch;
        }
        return bestMatch;
    }

    public static int calcBeamQuantity (int customerLength, ConnectionPool connectionPool){


        int beam = calcBeams(customerLength, connectionPool);
        List<Integer> beamList = new ArrayList<>();
        if (customerLength>600){
            beamList.add(beam);
            beamList.add(beam);
            beamList.add(beam);
            beamList.add(beam);

        } else {
            beamList.add(beam);
            beamList.add(beam);

        }



        return beamList.size();
    }


    public static int calcPoles ( int customerLength){
        int numberOfPosts=4;
        int maxDistanceBetweenPoles = 330;

        if (customerLength>maxDistanceBetweenPoles){
            numberOfPosts=6;
        }

        return numberOfPosts;
    }

    private static int calcRafters ( int customerLength){
        int rafters = 0;
        for (double i = 0; i < customerLength; i += (55 + 4.5)) {
            rafters ++;
        }

        return rafters;
    }

}
