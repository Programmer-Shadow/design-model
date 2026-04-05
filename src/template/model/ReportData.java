package template.model;

public class ReportData {
    private final String orderId;
    private final String product;
    private final int quantity;
    private final double total;

    public ReportData(String orderId, String product, int quantity, double total) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.total = total;
    }

    public String getOrderId()  { return orderId; }
    public String getProduct()  { return product; }
    public int getQuantity()    { return quantity; }
    public double getTotal()    { return total; }
}
