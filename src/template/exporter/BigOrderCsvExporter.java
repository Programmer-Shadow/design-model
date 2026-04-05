package template.exporter;

import template.model.ReportData;

import java.util.ArrayList;
import java.util.List;

/**
 * 钩子方法的实际应用：覆盖 filterData()，只导出 total > 5000 的大额订单。
 * 导出格式复用 CsvReportExporter 的 header/body/footer 逻辑。
 */
public class BigOrderCsvExporter extends CsvReportExporter {

    @Override
    public String exporterName() { return "BigOrderCsvExporter"; }

    @Override
    protected List<ReportData> filterData(List<ReportData> data) {
        List<ReportData> result = new ArrayList<>();
        for (ReportData row : data) {
            if (row.getTotal() > 5000) {
                result.add(row);
            }
        }
        return result;
    }
}
