package app.persistence;

import app.entities.Variant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laith kaseb
 **/


public class VariantMapper {

    public static List<Variant> selectVariantLengthById(int materialId, ConnectionPool connectionPool){

        List<Variant> variant=new ArrayList<>();
        String sql = "select length from variant where material_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1,materialId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()){

                int length = rs.getInt("length");

                variant.add(new Variant(length));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return variant;
    }

    public static Variant selectvariantidByLength(int bestMatch, ConnectionPool connectionPool) {
        Variant variant=null;
        String sql="select * from variant where length=?";
        try (
                Connection connection= connectionPool.getConnection();
                PreparedStatement ps=connection.prepareStatement(sql);
        ){
            ps.setInt(1,bestMatch);

            ResultSet  resultSet= ps.executeQuery();
            while (resultSet.next()){
                int variantId =resultSet.getInt("variant_id");
                int materialId = resultSet.getInt("material_id");
                int length = resultSet.getInt("length");
                variant = new Variant(variantId,materialId,length);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return variant;
    }
}
