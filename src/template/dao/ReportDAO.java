package template.dao;

import template.model.ReportData;
import java.util.Arrays;
import java.util.List;

public class ReportDAO {
    public List<ReportData> findAll() {
        return Arrays.asList(
            new ReportData("ORD-001", "MacBook Pro",  1, 12999.0),
            new ReportData("ORD-002", "iPhone 15",    2,  7998.0),
            new ReportData("ORD-003", "AirPods Pro",  3,  4497.0),
            new ReportData("ORD-004", "iPad Air",     1,  4799.0),
            new ReportData("ORD-005", "Apple Watch",  2,  6398.0)
        );
    }
}
