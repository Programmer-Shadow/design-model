package template.exporter;

import template.model.ReportData;
import java.util.List;

public class CsvReportExporter extends ReportExporter {

    @Override
    public String exporterName() { return "CsvReportExporter"; }

    @Override
    protected String buildHeader() {
        return "订单号,商品,数量,总价\n";
    }

    @Override
    protected String buildBody(List<ReportData> data) {
        StringBuilder sb = new StringBuilder();
        for (ReportData row : data) {
            sb.append(row.getOrderId()).append(",")
              .append(row.getProduct()).append(",")
              .append(row.getQuantity()).append(",")
              .append(row.getTotal()).append("\n");
        }
        return sb.toString();
    }

    @Override
    protected String buildFooter() {
        return "";
    }
}
