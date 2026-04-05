package abstractfactory.product.dark;

import abstractfactory.product.Alert;

public class DarkAlert implements Alert {
    @Override
    public String render() {
        return "<div style='padding:12px 16px;border-radius:8px;border:1px solid #065f46;"
                + "background:#064e3b;color:#6ee7b7;font-size:13px;'>"
                + "✅ 操作成功（Dark 主题提示）"
                + "</div>";
    }

    @Override
    public String name() { return "DarkAlert"; }
}
