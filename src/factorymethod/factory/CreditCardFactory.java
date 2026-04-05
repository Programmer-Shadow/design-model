package factorymethod.factory;

import factorymethod.product.CreditCardProcessor;
import factorymethod.product.PaymentProcessor;

public class CreditCardFactory extends PaymentFactory {
    @Override
    protected PaymentProcessor createProcessor() {
        return new CreditCardProcessor();
    }

    @Override
    public String factoryName() { return "CreditCardFactory"; }
}
