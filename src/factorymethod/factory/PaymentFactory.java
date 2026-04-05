package factorymethod.factory;

import factorymethod.product.PaymentProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 工厂方法模式 — 抽象工厂基类
 *
 * createProcessor() 是工厂方法：由子类决定实例化哪种处理器。
 * processPayment() 是业务方法：内部调用工厂方法获取处理器再执行支付。
 */
public abstract class PaymentFactory {

    private final List<String> steps = new ArrayList<>();

    // 工厂方法：子类决定创建哪种处理器
    protected abstract PaymentProcessor createProcessor();

    public abstract String factoryName();

    // 业务方法：使用工厂方法创建的对象完成支付
    public String processPayment(double amount) {
        steps.clear();
        steps.add(factoryName() + ".processPayment(" + amount + ")");
        steps.add("→ createProcessor()  [工厂方法，由 " + factoryName() + " 实现]");
        PaymentProcessor processor = createProcessor();
        steps.add("→ 创建了 " + processor.name());
        steps.add("→ " + processor.name() + ".pay(" + amount + ")");
        String result = processor.pay(amount);
        steps.add("→ " + result);
        return result;
    }

    public List<String> getLastSteps() {
        return Collections.unmodifiableList(steps);
    }

    public String getProcessorName() {
        return createProcessor().name();
    }
}
