package abstractfactory.product.light;

import abstractfactory.product.Card;

public class LightCard implements Card {
    @Override
    public String render() {
        return "<div style='padding:20px;border:1px solid #e5e7eb;border-radius:12px;"
                + "background:#fff;color:#1f2937;box-shadow:0 2px 8px rgba(0,0,0,.06);'>"
                + "<h3 style='margin:0 0 8px;font-size:16px;'>浅色卡片</h3>"
                + "<p style='margin:0;font-size:13px;color:#6b7280;'>这是 Light 主题的卡片组件，背景为白色，文字为深色。</p>"
                + "</div>";
    }

    @Override
    public String name() { return "LightCard"; }
}
