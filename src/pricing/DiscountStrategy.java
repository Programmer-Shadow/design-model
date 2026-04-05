package pricing;

/**
 * 策略接口：
 * 所有价格计算规则都要实现这个统一入口。
 */
public interface DiscountStrategy {
    double calculate(Order order);
}
