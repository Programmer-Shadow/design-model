package pricing;

public class NormalPriceStrategy implements DiscountStrategy {
    @Override
    public double calculate(Order order) {
        // 原价策略：不做任何折扣。
        return order.getOriginTotal();
    }
}
