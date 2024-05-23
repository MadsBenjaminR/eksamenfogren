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
        // Route til at se materiale
        app.get("/material-list", ctx -> getMaterials(ctx, connectionPool));

        //Route til render af material siden efter et edit
        app.get("/materials.html", ctx -> getMaterialListPage(ctx, connectionPool));

        //Route til at redigere materiale
        app.post("/editmaterial.html", ctx -> editMaterial(ctx, connectionPool));

        // Route til at slette materiale
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
            // Hent materialeobjektet ud fra databasen
            Material material = MaterialMapper.getMaterialById(materialId, connectionPool);

            // Sæt det i et sessionattribute
            ctx.attribute("material", material);

            // Render editmaterial.html siden
            ctx.render("/editmaterial.html");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Bad Request: Invalid input format");
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }

    // Method til at slette materiale
    private static void deleteMaterial(Context ctx, ConnectionPool connectionPool) {
        try {
            int materialId = Integer.parseInt(ctx.formParam("materialId"));

            // Kald metode der sletter materiale fra database
            MaterialMapper.deleteMaterial(materialId, connectionPool);

            ctx.render("/editmaterial.html");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Bad Request: Invalid input format");
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }
    private static void saveEditMaterial(Context ctx, ConnectionPool connectionPool) {
        try {
            // Træk form data ud
            int materialId = Integer.parseInt(ctx.formParam("materialId"));
            String name = ctx.formParam("name");
            int width = Integer.parseInt(ctx.formParam("width"));
            int height = Integer.parseInt(ctx.formParam("height"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));
            String unit = ctx.formParam("unit");
            float retailPrice = Float.parseFloat(ctx.formParam("retailPrice"));
            float purchasePrice = Float.parseFloat(ctx.formParam("purchasePrice"));

            // Opdater materiale i databasen
            MaterialMapper.updateMaterial(materialId, name, width, height, quantity, unit, retailPrice, purchasePrice, connectionPool);

            ctx.redirect("/materials.html");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Bad Request: Invalid input format");
        } catch (DatabaseException e) {
            ctx.status(500).result("Internal Server Error");
        }
    }
}