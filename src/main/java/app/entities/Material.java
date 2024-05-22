package app.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class Material {
    private int materialId;
    private String name;
    private int materialWidth;
    private int materialLength;
    private int materialHeight;
    private int quantity;
    private String unit;
    private float retailPrice;
    private float purchasePrice;
    private static List<Material> materials = new ArrayList<>();

    public Material(int materialId, String name, int materialWidth, int materialLength, int materialHeight, int quantity, String unit, float retailPrice, float purchasePrice) {
        this.materialId = materialId;
        this.name = name;
        this.materialWidth = materialWidth;
        this.materialLength = materialLength;
        this.materialHeight = materialHeight;
        this.quantity = quantity;
        this.unit = unit;

        this.retailPrice = retailPrice;
        this.purchasePrice = purchasePrice;
    }


    public Material(int materialId, String name, int materialWidth, int materialHeight, int quantity, String unit, float retailPrice, float purchasePrice) {
        this.materialId = materialId;
        this.name = name;
        this.materialWidth = materialWidth;
        this.materialHeight = materialHeight;
        this.quantity = quantity;
        this.unit = unit;
        this.retailPrice = retailPrice;
        this.purchasePrice = purchasePrice;
    }

    public static List<Material> getMaterials() {
        return materials;
    }

    public int getMaterialId() {
        return materialId;
    }

    public String getName() {
        return name;
    }

    public int getMaterialWidth() {
        return materialWidth;
    }

    public int getMaterialLength() {
        return materialLength;
    }

    public int getMaterialHeight() {
        return materialHeight;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }


    public float getRetailPrice() {
        return retailPrice;
    }

    public float getPurchasePrice() {
        return purchasePrice;
    }
}
