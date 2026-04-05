package abstractfactory.product.dark;

import abstractfactory.product.Button;

public class DarkButton implements Button {
    @Override
    public String render() {
        return "<button style='padding:10px 24px;border:1px solid #475569;border-radius:8px;"
                + "background:#334155;color:#f1f5f9;font-size:14px;cursor:pointer;'>深色按钮</button>";
    }

    @Override
    public String name() { return "DarkButton"; }
}
