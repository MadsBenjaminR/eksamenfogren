package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class ContactInformationMapper {
   /* public static void getAllInfo(ConnectionPool connectionPool){

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {



            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Der opstod en fejl , prøv igen");
            }

        } catch (SQLException e) {
            String msg = "Der opstod en fejl ved opdatering af materialet. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }

    }

    */
}
