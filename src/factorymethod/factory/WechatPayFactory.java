package factorymethod.factory;

import factorymethod.product.PaymentProcessor;
import factorymethod.product.WechatPayProcessor;

public class WechatPayFactory extends PaymentFactory {
    @Override
    protected PaymentProcessor createProcessor() {
        return new WechatPayProcessor();
    }

    @Override
    public String factoryName() { return "WechatPayFactory"; }
}
