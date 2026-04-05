package factorymethod.product;

public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public String pay(double amount) {
        double fee = Math.round(amount * 0.006 * 100.0) / 100.0;
        return "信用卡支付 " + amount + " 元（手续费 " + fee + " 元）— 成功";
    }

    @Override
    public String name() { return "CreditCardProcessor"; }
}
