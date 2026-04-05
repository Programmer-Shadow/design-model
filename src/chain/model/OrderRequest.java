package chain.model;

public class OrderRequest {
    private final String product;
    private final int quantity;
    private final double unitPrice;

    public OrderRequest(String product, int quantity, double unitPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProduct()   { return product; }
    public int getQuantity()     { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double totalAmount()  { return unitPrice * quantity; }
}
