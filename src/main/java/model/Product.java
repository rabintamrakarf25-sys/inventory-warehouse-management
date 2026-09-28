package model;

public class Product implements Stockable {

    private int id;
    private String sku;
    private String name;
    private String category;
    private int quantity;
    private double unitPrice;
    private int reorderThreshold;

    public Product() {
    }

    public Product(
            int id,
            String sku,
            String name,
            String category,
            int quantity,
            double unitPrice,
            int reorderThreshold) {

        this.id = id;
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.reorderThreshold = reorderThreshold;
    }

    public Product(
            String sku,
            String name,
            String category,
            int quantity,
            double unitPrice,
            int reorderThreshold) {

        this.sku = sku;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.reorderThreshold = reorderThreshold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(int reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    @Override
    public double calculateStockValue() {
        return quantity * unitPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", reorderThreshold=" + reorderThreshold +
                '}';
    }
}