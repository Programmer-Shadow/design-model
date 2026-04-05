package factorymethod.product;

public class AlipayProcessor implements PaymentProcessor {
    @Override
    public String pay(double amount) {
        return "支付宝扫码支付 " + amount + " 元 — 成功";
    }

    @Override
    public String name() { return "AlipayProcessor"; }
}
