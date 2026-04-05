package strategy.service.pricing;

import strategy.model.Order;

import java.util.HashMap;
import java.util.Map;

public class DiscountContext {
    // 上下文对象：维护"策略名 -> 策略实现"的映射，并负责调度。
    private final Map<String, DiscountStrategy> strategyMap = new HashMap<>();

    public DiscountContext() {
        // 在一个位置集中注册所有策略，避免业务流程里出现大量 if-else。
        strategyMap.put("normal", new NormalPriceStrategy());
        strategyMap.put("vip", new VipDiscountStrategy());
        strategyMap.put("new-user", new NewUserDiscountStrategy());
    }

    public double calculateTotal(String strategyName, Order order) {
        // 当策略名不存在时，回退到 normal，保证系统可用性。
        DiscountStrategy strategy = strategyMap.getOrDefault(strategyName, strategyMap.get("normal"));
        return strategy.calculate(order);
    }
}
