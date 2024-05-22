package app.entities;


public class Orderline {

    private int orderlineId;
    private int length;
    private int width;
    private int quantity;
    private String unit;
    private String functionalDescription;
    private int price;
    private int variantIdFk;
    private int orderIdFk;
    private Variant variant;
    private Material material;
    private Orders orders;

   /*
    public Orderline(int orderlineId, String description, int length, int quantity, String unit, String functionalDescription, int variantIdFk, int orderIdFk) {
        this.orderlineId = orderlineId;
        this.description = description;
        this.length = length;
        this.quantity = quantity;
        this.unit = unit;
        this.functionalDescription = functionalDescription;
        this.variantIdFk = variantIdFk;
        this.orderIdFk = orderIdFk;
    }

    public Orderline(String functionalDescription, String description, int length, int quantity, String unit) {
        this.functionalDescription = functionalDescription;
        this.description = description;
        this.length = length;
        this.quantity = quantity;
        this.unit = unit;
    }
*/

    public Orderline(int width, int length, int price, String unit, String functionalDescription){
        this.width = width;
        this.length = length;
        this.price = price;
        this.unit = unit;
        this.functionalDescription = functionalDescription;
    }

    public Orderline(int orderlineId, int quantity, String unit, String functionalDescription, Variant variant, Material material) {
        this.orderlineId = orderlineId;
        this.quantity = quantity;
        this.unit = unit;
        this.functionalDescription = functionalDescription;
        this.variantIdFk = variantIdFk;
        this.orderIdFk = orderIdFk;
        this.variant = variant;
        this.material = material;
    }

    public Orderline(int orderlineId, int quantity, String unit, String functionalDescription, Variant variant, Material material, Orders orders) {
        this.orderlineId=orderlineId;
        this.quantity=quantity;
        this.unit=unit;
        this.functionalDescription=functionalDescription;
        this.variant=variant;
        this.material=material;
        this.orders=orders;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public int getOrderlineId() {
        return orderlineId;
    }


    public int getLength() {
        return length;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getFunctionalDescription() {
        return functionalDescription;
    }

    public int getVariantIdFk() {
        return variantIdFk;
    }

    public int getOrderIdFk() {
        return orderIdFk;
    }

    public int getWidth(){
        return width;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
