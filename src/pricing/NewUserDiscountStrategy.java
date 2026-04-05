package pricing;

public class NewUserDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(Order order) {
        // 新用户策略：直减 20，最低不小于 0。
        double total = order.getOriginTotal() - 20;
        return Math.max(total, 0);
    }
}
