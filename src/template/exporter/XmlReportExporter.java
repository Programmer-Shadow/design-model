package template.exporter;

import template.model.ReportData;
import java.util.List;

public class XmlReportExporter extends ReportExporter {

    @Override
    public String exporterName() { return "XmlReportExporter"; }

    @Override
    protected String buildHeader() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<orders>\n";
    }

    @Override
    protected String buildBody(List<ReportData> data) {
        StringBuilder sb = new StringBuilder();
        for (ReportData row : data) {
            sb.append("  <order>\n")
              .append("    <orderId>").append(row.getOrderId()).append("</orderId>\n")
              .append("    <product>").append(row.getProduct()).append("</product>\n")
              .append("    <quantity>").append(row.getQuantity()).append("</quantity>\n")
              .append("    <total>").append(row.getTotal()).append("</total>\n")
              .append("  </order>\n");
        }
        return sb.toString();
    }

    @Override
    protected String buildFooter() {
        return "</orders>";
    }
}
