package abstractfactory.factory;

import abstractfactory.product.Alert;
import abstractfactory.product.Button;
import abstractfactory.product.Card;

/**
 * 抽象工厂接口：定义一族产品的创建方法。
 * 每个具体工厂实现这组方法，保证产出的 Button、Card、Alert 风格一致。
 */
public interface UIFactory {
    Button createButton();
    Card createCard();
    Alert createAlert();
    String factoryName();
}
