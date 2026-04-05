package strategy.service;

import strategy.dao.OrderDAO;
import strategy.model.Order;
import strategy.service.pricing.DiscountContext;

// Service 层：编排业务逻辑，不感知 HTTP 细节。
public class PricingService {
    private final DiscountContext discountContext = new DiscountContext();
    private final OrderDAO orderDAO;

    public PricingService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }
    //定额
    public double quote(String strategyName, Order order) {
        double total = discountContext.calculateTotal(strategyName, order);
        // 计算完成后持久化订单记录
        orderDAO.save(order);
        return total;
    }
}
