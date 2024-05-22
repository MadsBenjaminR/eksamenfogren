package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class OrderMapper {

    public static Orders getOrderFromUserId;

    public static List<Orders> getAllFromOrderId(int orderId, ConnectionPool connectionPool) {

        List<Orders> ordersList=new ArrayList<>();

        String sql = "select * from orders inner join public.users u on orders.user_id_fk = u.user_id inner join public.contact_information ci on ci.customer_id = u.contact_id_fk where order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1,orderId);

            ResultSet rs= ps.executeQuery();
            while (rs.next()) {

                //ORDER
                int customerWidth = rs.getInt("customer_width");
                int customerLength = rs.getInt("customer-length");
                String status = rs.getString("status");

                //CONTACTINFORMATION
                String name=rs.getString("name");
                int zip = rs.getInt("zip_code_fk");

                ContactInformation contactInformation = new ContactInformation(name,zip);

                Orders order=new Orders(customerWidth, customerLength, status,contactInformation );
                ordersList.add(order);

            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Der opstod en fejl ved opdatering af materialet. Prøv igen");

        }
        return ordersList;
    }

    public static List<Orders> getAllOrders(ConnectionPool connectionPool) {

        List<Orders> ordersList=new ArrayList<>();

        String sql = "select * from orders inner join public.users u on orders.user_id_fk = u.user_id inner join public.contact_information ci on ci.customer_id = u.contact_id_fk";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {


            ResultSet rs= ps.executeQuery();
            while (rs.next()) {

                //ORDER
                int orderId= rs.getInt("order_id");
                int customerWidth = rs.getInt("customer_width");
                int customerLength = rs.getInt("customer_length");
                String status = rs.getString("status");

                //CONTACTINFORMATION
                String name=rs.getString("name");
                int zip = rs.getInt("zip_code_fk");

                ContactInformation contactInformation = new ContactInformation(name,zip);

                Orders order=new Orders(orderId,customerWidth, customerLength, status,contactInformation );
                ordersList.add(order);

            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Der opstod en fejl ved opdatering af materialet. Prøv igen");

        }
        return ordersList;
    }

    public static void updateTotalPrice(float totalPrice, int orderId,ConnectionPool connectionPool) throws DatabaseException{

        String sql="Update orders set status = 'Tilbud sendt', total_price=? where order_id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setFloat(1, totalPrice);
            ps.setInt(2, orderId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected!=1){
                throw new DatabaseException("Fejl ved opdatering af totalpris");
            }
        }catch(SQLException e) {
            String msg = "databasefejl";
            throw new DatabaseException(msg, e.getMessage());
        }
    }


    public static boolean doesOrderByUserIdExist(int userIdFk, ConnectionPool connectionPool) {
        boolean orderExists = false;
        String sql = "select COUNT(*) as count from orders where user_id_fk = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userIdFk);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                orderExists = count > 0;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderExists;
    }

    public static Orders getCustomerDim(int userId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "select customer_width, customer_length, status from orders where user_id_fk= ? and status= 'Tilbud sendt'";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1,userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                int customerWidth = rs.getInt("customer_width");
                int customerLength = rs.getInt("customer_length");
                String status = rs.getString("status");

                return new Orders(customerWidth,customerLength, status);
            } else {
                throw new DatabaseException("Fejl. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Orders> getOrderFromUserId(int userId, ConnectionPool connectionPool) throws DatabaseException {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "select order_id,status, customer_width, customer_length,total_price from orders where user_id_fk = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String status = rs.getString("status");
                int width = rs.getInt("customer_width");
                int length = rs.getInt("customer_length");
                int price = rs.getInt("total_price");
                int orderId=rs.getInt("order_id");


                Orders orders = new Orders(orderId,status, width, length, price);
                ordersList.add(orders);
            }


        } catch (SQLException e) {
            throw new DatabaseException("ordre kunne ikke findes med userid'et");
        }
        return ordersList;
    }

    public static Orders getGetOrderFromUserId(int user, ConnectionPool connectionPool) {
        Orders orders = null;
        String sql = "select status, customer_width, customer_length,total_price from orders where user_id_fk = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String status = rs.getString("status");
                int width = rs.getInt("customer_width");
                int length = rs.getInt("customer_length");
                int price = rs.getInt("total_price");
                orders = new Orders(status, width, length, price);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }
}
