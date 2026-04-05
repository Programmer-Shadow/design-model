package template.exporter;

import template.model.ReportData;
import java.util.List;

public class JsonReportExporter extends ReportExporter {

    @Override
    public String exporterName() { return "JsonReportExporter"; }

    @Override
    protected String buildHeader() {
        return "[\n";
    }

    @Override
    protected String buildBody(List<ReportData> data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            ReportData row = data.get(i);
            sb.append("  {")
              .append("\"orderId\":\"").append(row.getOrderId()).append("\",")
              .append("\"product\":\"").append(row.getProduct()).append("\",")
              .append("\"quantity\":").append(row.getQuantity()).append(",")
              .append("\"total\":").append(row.getTotal())
              .append("}");
            if (i < data.size() - 1) sb.append(",");
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    protected String buildFooter() {
        return "]";
    }
}
