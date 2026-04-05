package abstractfactory.product.light;

import abstractfactory.product.Button;

public class LightButton implements Button {
    @Override
    public String render() {
        return "<button style='padding:10px 24px;border:1px solid #d1d5db;border-radius:8px;"
                + "background:#fff;color:#1f2937;font-size:14px;cursor:pointer;'>浅色按钮</button>";
    }

    @Override
    public String name() { return "LightButton"; }
}
