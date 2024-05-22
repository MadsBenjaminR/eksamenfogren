package app.persistence;

import app.entities.Material;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialMapper {



    public static List<Material> getAllMaterials(ConnectionPool connectionPool) throws SQLException {

        List<Material> materials = new ArrayList<>();

        String sql = "SELECT * FROM public.material ORDER BY material_id ASC";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    int materialId = rs.getInt("material_id");
                    String name = rs.getString("name");
                    int width = rs.getInt("material_width");
                    int height = rs.getInt("material_height");
                    int quantity = rs.getInt("quantity");
                    String unit = rs.getString("unit");
                    float retailPrice = rs.getFloat("retail_price");
                    float purchasePrice = rs.getFloat("purchase_price");
                    materials.add(new Material(materialId, name, width, height, quantity, unit, retailPrice, purchasePrice));
                }
            }
        }
        return materials;
    }

    public static void updateMaterial(int materialId, String name, int width, int height, int quantity, String unit, float retailPrice, float purchasePrice, ConnectionPool connectionPool) throws DatabaseException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE material SET name = ?, material_width = ?, material_height = ?, quantity = ?, unit = ?, retail_price = ?, purchase_price = ? WHERE material_id = ?")) {
            ps.setString(1, name);
            ps.setInt(2, width);
            ps.setInt(3, height);
            ps.setInt(4, quantity);
            ps.setString(5, unit);
            ps.setFloat(6, retailPrice);
            ps.setFloat(7, purchasePrice);
            ps.setInt(8, materialId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Failed to update material");
            }

        } catch (SQLException e) {
            // Rollback the transaction if an exception occurs
            throw new DatabaseException("Error updating material", e);
        }
    }

    public static void deleteMaterial(int materialId, ConnectionPool connectionPool) throws DatabaseException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE * FROM material WHERE material_id = ?")) {
            ps.setInt(1, materialId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Failed to delete material");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting material", e);
        }
    }

    public static Material getMaterialById(int materialId, ConnectionPool connectionPool) throws DatabaseException {
        Material material = null;
        String sql = "SELECT * FROM material WHERE material_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    materialId = rs.getInt("material_id");
                    String name = rs.getString("name");
                    int width = rs.getInt("material_width");
                    int height = rs.getInt("material_height");
                    int quantity = rs.getInt("quantity");
                    String unit = rs.getString("unit");
                    float retailPrice = rs.getFloat("retail_price");
                    float purchasePrice = rs.getFloat("purchase_price");
                    material = new Material(materialId, name, width, height, quantity, unit, retailPrice, purchasePrice);
                } else {
                    throw new DatabaseException("Material not found for id: " + materialId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving material", e);
        }
        return material;
    }

}