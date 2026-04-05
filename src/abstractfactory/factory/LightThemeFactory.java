package abstractfactory.factory;

import abstractfactory.product.Alert;
import abstractfactory.product.Button;
import abstractfactory.product.Card;
import abstractfactory.product.light.LightAlert;
import abstractfactory.product.light.LightButton;
import abstractfactory.product.light.LightCard;

public class LightThemeFactory implements UIFactory {
    @Override
    public Button createButton() { return new LightButton(); }

    @Override
    public Card createCard() { return new LightCard(); }

    @Override
    public Alert createAlert() { return new LightAlert(); }

    @Override
    public String factoryName() { return "LightThemeFactory"; }
}
