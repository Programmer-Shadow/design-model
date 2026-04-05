package template.exporter;

import template.model.ReportData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 模板方法模式 — 抽象基类
 *
 * export() 是模板方法：固定导出骨架，具体步骤委托给子类。
 * filterData() 是钩子方法：子类可选择性重写，默认不过滤。
 */
public abstract class ReportExporter {

    private final List<String> steps = new ArrayList<>();

    // 模板方法：final 禁止子类改变流程
    public final String export(List<ReportData> data) {
        steps.clear();

        steps.add("① filterData()   [钩子方法 — 子类可选覆盖，默认透传]");
        List<ReportData> filtered = filterData(data);

        steps.add("② buildHeader()  [抽象方法 — 子类必须实现]");
        String header = buildHeader();

        steps.add("③ buildBody()    [抽象方法 — 子类必须实现]");
        String body = buildBody(filtered);

        steps.add("④ buildFooter()  [抽象方法 — 子类必须实现]");
        String footer = buildFooter();

        steps.add("⑤ assemble()     [模板方法内部组装 → 完成]");
        return header + body + footer;
    }

    // 钩子方法：子类按需覆盖
    protected List<ReportData> filterData(List<ReportData> data) {
        return data;
    }

    protected abstract String buildHeader();
    protected abstract String buildBody(List<ReportData> data);
    protected abstract String buildFooter();

    public List<String> getLastSteps() {
        return Collections.unmodifiableList(steps);
    }

    // 子类通过此方法暴露自己的类名，便于前端展示
    public abstract String exporterName();
}
