package strategy.model;

// 订单实体：作为策略计算的输入参数。
public class Order {
    private final double unitPrice;
    private final int quantity;

    public Order(double unitPrice, int quantity) {
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getOriginTotal() {
        // 原始总价 = 单价 * 数量
        return unitPrice * quantity;
    }
}
