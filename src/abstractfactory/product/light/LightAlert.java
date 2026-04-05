package abstractfactory.product.light;

import abstractfactory.product.Alert;

public class LightAlert implements Alert {
    @Override
    public String render() {
        return "<div style='padding:12px 16px;border-radius:8px;border:1px solid #bbf7d0;"
                + "background:#f0fdf4;color:#166534;font-size:13px;'>"
                + "✅ 操作成功（Light 主题提示）"
                + "</div>";
    }

    @Override
    public String name() { return "LightAlert"; }
}
