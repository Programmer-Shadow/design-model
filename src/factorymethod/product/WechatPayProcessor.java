package factorymethod.product;

public class WechatPayProcessor implements PaymentProcessor {
    @Override
    public String pay(double amount) {
        return "微信扫码支付 " + amount + " 元 — 成功";
    }

    @Override
    public String name() { return "WechatPayProcessor"; }
}
