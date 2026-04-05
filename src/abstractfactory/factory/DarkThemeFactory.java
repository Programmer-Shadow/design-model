package abstractfactory.factory;

import abstractfactory.product.Alert;
import abstractfactory.product.Button;
import abstractfactory.product.Card;
import abstractfactory.product.dark.DarkAlert;
import abstractfactory.product.dark.DarkButton;
import abstractfactory.product.dark.DarkCard;

public class DarkThemeFactory implements UIFactory {
    @Override
    public Button createButton() { return new DarkButton(); }

    @Override
    public Card createCard() { return new DarkCard(); }

    @Override
    public Alert createAlert() { return new DarkAlert(); }

    @Override
    public String factoryName() { return "DarkThemeFactory"; }
}
