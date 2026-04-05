package strategy.service.pricing;

import strategy.model.Order;

public class VipDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(Order order) {
        // VIP 策略：9 折。
        return order.getOriginTotal() * 0.9;
    }
}
