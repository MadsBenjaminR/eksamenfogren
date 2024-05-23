package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class OrderlineMapper {
    public static List<Orderline> getOrderline(int orderId, ConnectionPool connectionPool)
    {

        List<Orderline> orderlines = new ArrayList<>();

        // String sql = "SELECT orderline.functional_description, orderline.description, orderline.length, orderline.quantity, orderline.unit from public.orderline JOIN orders ON orderline.order_id = orders.order_id JOIN users ON orders.user_id_fk = users.user_id";
        //  String sql = "SELECT orders.customer_width, orders.customer_length, orderline.unit, orderline.functional_description FROM orders JOIN orderline ON orders.order_id = orderline.order_id WHERE order_id=?";
        String sql = "SELECT o.customer_width, o.customer_length, o.total_price, ol.unit, ol.functional_description " +
                "FROM orders o " +
                "JOIN orderline ol ON o.order_id = ol.order_id " +
                "WHERE ol.order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1,orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {

                int width= rs.getInt("customer_width");
                int length = rs.getInt("customer_length");
                int price = rs.getInt("total_price");
                String unit = rs.getString("unit");
                String functionalDescription = rs.getString("functional_description");

                Orderline orderline = new Orderline(width, length, price, unit, functionalDescription);
                orderlines.add(orderline);
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return orderlines;
    }

    public static void insertIntoOrderline(int width, int length, String roof, int height, int variantId, int orderId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "insert into orderline (carport_width, carport_length, carport_roof, carport_height, variant_id_fk, order_id," +
                "functional_description)" +
                "values (?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1,width );
            ps.setInt(2, length);
            ps.setString(3, roof);
            ps.setInt(4, height);
            ps.setInt(5, variantId);
            ps.setInt(6, orderId);
            ps.setInt(7, variantId);


            ps.executeUpdate();


        }catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static int makeAnOrder(String name, String status, LocalDate date, User user, int width, int length, ConnectionPool connectionPool) throws SQLException, DatabaseException {

        int orderId = 0;

        String sql="insert into orders(name, status, dato, user_id_fk, customer_width, customer_length) values (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        )

        {
            ps.setString(1,name);
            ps.setString(2, status);
            ps.setDate(3, Date.valueOf(date));
            ps.setInt(4,user.getUserId());
            ps.setInt(5, width);
            ps.setInt(6, length);


            int rowsAffected = ps.executeUpdate();

            if (rowsAffected>0){
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                orderId = rs.getInt("order_id");
                return orderId;
            }


        }catch (SQLException e){
            throw new DatabaseException("fejl i data");
        }

        return orderId;
    }


    public static int insertPartIntoOrderline(int variantIdFk, int orderId, String functionalDescription,int quantity,ConnectionPool connectionPool){
        int orderlineId = 0;

        String sql = "insert into orderline (variant_id_fk, order_id, functional_description, quantity, unit) values (?, ?, ?, ?,'Stk')";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            ps.setInt(1, variantIdFk);
            ps.setInt(2, orderId);
            ps.setString(3,functionalDescription);
            ps.setInt(4, quantity);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected>0){
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                orderlineId = rs.getInt("orderline_id");
                return orderlineId;
            }



        }catch (SQLException e){
            e.printStackTrace();

        }
        return orderlineId;
    }

    public static List<Orderline> getPartsListPrice(int userId,ConnectionPool connectionPool){
        List<Orderline> orderline=new ArrayList<>();
        Variant variant=null;
        Material material=null;
        Orders orders = null;

        String sql = "SELECT * FROM orderline JOIN variant ON orderline.variant_id_fk = variant.variant_id JOIN material ON variant.material_id = material.material_id JOIN orders on orderline.order_id = orders.order_id JOIN users on orders.user_id_fk= users.user_id Where users.user_id =?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                //Orderline
                int orderlineId = rs.getInt("orderline_id");
                int variantIdFk = rs.getInt("variant_id_fk");
                String functionalDescription = rs.getString("functional_description");
                int quantity = rs.getInt("quantity");
                String unit = rs.getString("unit");


                //Variant
                int variantId = rs.getInt("variant_id");
                int materialIdFk = rs.getInt("material_id");
                int length = rs.getInt("length");
                variant = new Variant(variantId, materialIdFk, length);

                //Material
                int materialId = rs.getInt("material_id");
                String name = rs.getString("name");
                int materialLength = rs.getInt("material_length");
                int materialWidth = rs.getInt("material_width");
                int materialHeight = rs.getInt("material_height");
                int materialQuantity = rs.getInt("quantity");
                String materialUnit = rs.getString("unit");
                float retailPrice = rs.getFloat("retail_price");
                float purchasePrice = rs.getFloat("purchase_price");
                material = new Material(materialId, name, materialWidth, materialLength, materialHeight, materialQuantity, materialUnit, retailPrice, purchasePrice);

                //Order
                int orderId = rs.getInt("order_id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                //LocalDate date = rs.getDate("dato").toLocalDate();
                int userIdFk = rs.getInt("user_id_fk");
                int customerWidth = rs.getInt("customer_width");
                int customerLength = rs.getInt("customer_length");
                int totalPrice = rs.getInt("total_price");
                orders = new Orders(orderId, name, status, userIdFk, customerWidth, customerLength, totalPrice);


                //User
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                int contactIdFk = rs.getInt("contact_id_fk");
                User user = new User(userId, email, password, role, contactIdFk);


                orderline.add(new Orderline(orderlineId, quantity, unit, functionalDescription, variant, material,orders));

                //orderline = new Orderline(orderlineId, description, quantity, unit, functionalDescription, variant, material);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderline;
    }

    public static List<Orderline> getPartsListByOrderId(int orderId,ConnectionPool connectionPool){
        List<Orderline> orderline=new ArrayList<>();
        Variant variant=null;
        Material material=null;
        Orders orders = null;

        String sql = "SELECT * FROM orderline JOIN variant ON orderline.variant_id_fk = variant.variant_id JOIN material ON variant.material_id = material.material_id JOIN orders on orderline.order_id = orders.order_id JOIN users on orders.user_id_fk= users.user_id Where orders.order_id =?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1,orderId);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                //Orderline
                int orderlineId = rs.getInt("orderline_id");
                int variantIdFk = rs.getInt("variant_id_fk");
                String functionalDescription = rs.getString("functional_description");
                int quantity = rs.getInt("quantity");
                String unit = rs.getString("unit");


                //Variant
                int variantId = rs.getInt("variant_id");
                int materialIdFk = rs.getInt("material_id");
                int length = rs.getInt("length");
                variant = new Variant(variantId, materialIdFk, length);

                //Material
                int materialId = rs.getInt("material_id");
                String name = rs.getString("name");
                int materialLength = rs.getInt("material_length");
                int materialWidth = rs.getInt("material_width");
                int materialHeight = rs.getInt("material_height");
                int materialQuantity = rs.getInt("quantity");
                String materialUnit = rs.getString("unit");
                float retailPrice = rs.getFloat("retail_price");
                float purchasePrice = rs.getFloat("purchase_price");
                material = new Material(materialId, name, materialWidth, materialLength, materialHeight, materialQuantity, materialUnit, retailPrice, purchasePrice);

                //Order

                String name = rs.getString("name");
                String status = rs.getString("status");
                //LocalDate date = rs.getDate("dato").toLocalDate();
                int userIdFk = rs.getInt("user_id_fk");
                int customerWidth = rs.getInt("customer_width");
                int customerLength = rs.getInt("customer_length");
                int totalPrice = rs.getInt("total_price");
                orders = new Orders(orderId, name, status, userIdFk, customerWidth, customerLength, totalPrice);


                //User
                int userId = rs.getInt("user_id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                int contactIdFk = rs.getInt("contact_id_fk");
                User user = new User(userId, email, password, role, contactIdFk);


                orderline.add(new Orderline(orderlineId, quantity, unit, functionalDescription, variant, material,orders));

                //orderline = new Orderline(orderlineId, description, quantity, unit, functionalDescription, variant, material);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderline;
    }
}


