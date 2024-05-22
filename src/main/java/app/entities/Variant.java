package app.entities;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class Variant {
    private int variantId;
    private int materialId;
    private int length;

    public Variant(int variantId, int materialId, int length) {
        this.variantId = variantId;
        this.materialId = materialId;
        this.length = length;
    }

    public Variant(int length) {
        this.length=length;
    }

    public int getVariantId() {
        return variantId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public int getLength() {
        return length;
    }
}
