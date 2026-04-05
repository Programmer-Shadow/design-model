package factorymethod.product;

public interface PaymentProcessor {
    String pay(double amount);
    String name();
}
