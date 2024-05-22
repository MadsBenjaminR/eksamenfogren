package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;

import java.sql.*;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class UserMapper {

    /**
     * Metoden har til formål at godkende brugerens email og kodeord, for at se om brugeren findes i databasen
     *
     */
    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "select user_id, email,password,role from users where email=? and password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String role = rs.getString("role");
                return new User(userId,email,password, role);
            } else {
                throw new DatabaseException("Fejl ved login. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    /**
     * Metoden har til formål at oprette en bruger i databasen
     *
     */

    public static int createUser(String name, String streetName,int houseNumber,String floor,int phoneNumber,int zipCode, ConnectionPool connectionPool) throws DatabaseException
    {
        int customerId = 0;

        String sql = "insert into contact_information (name, street_name, house_number, floor, phone_number, zip_code_fk) values (?,?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        )
        {
            ps.setString(1, name);
            ps.setString(2, streetName);
            ps.setInt(3, houseNumber);
            ps.setString(4, floor);
            ps.setInt(5, phoneNumber);
            ps.setInt(6, zipCode);


            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0)
            {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                customerId = rs.getInt("customer_id");
                return customerId;
            }
        }
        catch (SQLException e)
        {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value "))
            {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
        return customerId;
    }

    public static void createUserTable(String email, String password1,int customerId, ConnectionPool connectionPool) throws DatabaseException, SQLException {
        {
            String sql = "insert into users (email, password,contact_id_fk) values (?,?,?)";

            try (
                    Connection connection = connectionPool.getConnection();
                    PreparedStatement ps = connection.prepareStatement(sql)
            )
            {
                ps.setString(1, email);
                ps.setString(2, password1);
                ps.setInt(3, customerId);


                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1)
                {
                    throw new DatabaseException("Fejl ved oprettelse af ny bruger");
                }
            }
            catch (SQLException | DatabaseException e){

            }

        }
    }
}
