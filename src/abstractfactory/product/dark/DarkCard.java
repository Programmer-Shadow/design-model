package abstractfactory.product.dark;

import abstractfactory.product.Card;

public class DarkCard implements Card {
    @Override
    public String render() {
        return "<div style='padding:20px;border:1px solid #475569;border-radius:12px;"
                + "background:#1e293b;color:#e2e8f0;box-shadow:0 2px 8px rgba(0,0,0,.3);'>"
                + "<h3 style='margin:0 0 8px;font-size:16px;'>深色卡片</h3>"
                + "<p style='margin:0;font-size:13px;color:#94a3b8;'>这是 Dark 主题的卡片组件，背景为深蓝灰，文字为浅色。</p>"
                + "</div>";
    }

    @Override
    public String name() { return "DarkCard"; }
}
