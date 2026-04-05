package factorymethod.factory;

import factorymethod.product.AlipayProcessor;
import factorymethod.product.PaymentProcessor;

public class AlipayFactory extends PaymentFactory {
    @Override
    protected PaymentProcessor createProcessor() {
        return new AlipayProcessor();
    }

    @Override
    public String factoryName() { return "AlipayFactory"; }
}
