package strategy.dao;

import strategy.model.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// DAO 层：模拟订单持久化（内存实现，可替换为 DB）
public class OrderDAO {
    private final List<Order> store = new ArrayList<>();

    public void save(Order order) {
        store.add(order);
    }

    public List<Order> findAll() {
        return Collections.unmodifiableList(store);
    }
}
