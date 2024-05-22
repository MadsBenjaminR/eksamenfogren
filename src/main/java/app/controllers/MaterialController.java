package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.entities.Material;
import app.persistence.MaterialMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class MaterialController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        // Route for retrieving materials
        app.get("/material-list", ctx -> getMaterials(ctx, connectionPool));

        //route til render af material siden efter et edit
        app.get("/materials.html", ctx -> getMaterialListPage(ctx, connectionPool));

        // Route for editing a material (POST request)
        app.post("/editmaterial.html", ctx -> editMaterial(ctx, connectionPool));

        // Route for deleting a material (POST request)
        app.post("/deleteMaterial", ctx -> deleteMaterial(ctx, connectionPool));

        app.post("/savematerialchanges", ctx -> saveEditMaterial(ctx, connectionPool));
    }

    // Methode til at hente alle materialer
    private static void getMaterials(Context ctx, ConnectionPool connectionPool) {

        List<Material> materials = new ArrayList<>();

        try {

            materials = MaterialMapper.getAllMaterials(connectionPool);

            ctx.attribute("materials", materials);

            ctx.render("materials.html");

        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }

    private static void getMaterialListPage(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Material> materials = MaterialMapper.getAllMaterials(connectionPool);
            ctx.attribute("materials", materials);
            ctx.render("materials.html");
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }

    private static void editMaterial(Context ctx, ConnectionPool connectionPool) {
        try {
            int materialId = Integer.parseInt(ctx.formParam("materialId"));
            // Retrieve the material object from the database or any other source
            Material material = MaterialMapper.getMaterialById(materialId, connectionPool);

            // Add the material object to the context attributes
            ctx.attribute("material", material);

            // Render the editmaterial.html template
            ctx.render("/editmaterial.html");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Bad Request: Invalid input format");
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }

    // Method to delete a material
    private static void deleteMaterial(Context ctx, ConnectionPool connectionPool) {
        try {
            int materialId = Integer.parseInt(ctx.formParam("materialId"));

            // Call MaterialMapper method to delete the material from the database
            MaterialMapper.deleteMaterial(materialId, connectionPool);

            // Redirect to the materials page
            ctx.render("/editmaterial.html");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Bad Request: Invalid input format");
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }
    private static void saveEditMaterial(Context ctx, ConnectionPool connectionPool) {
        try {
            // Extract form data
            int materialId = Integer.parseInt(ctx.formParam("materialId"));
            String name = ctx.formParam("name");
            int width = Integer.parseInt(ctx.formParam("width"));
            int height = Integer.parseInt(ctx.formParam("height"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));
            String unit = ctx.formParam("unit");
            float retailPrice = Float.parseFloat(ctx.formParam("retailPrice"));
            float purchasePrice = Float.parseFloat(ctx.formParam("purchasePrice"));

            // Update material in the database
            MaterialMapper.updateMaterial(materialId, name, width, height, quantity, unit, retailPrice, purchasePrice, connectionPool);

            // Redirect back to materials list
            ctx.redirect("/materials.html");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Bad Request: Invalid input format");
        } catch (DatabaseException e) {
            ctx.status(500).result("Internal Server Error");
        }
    }
}