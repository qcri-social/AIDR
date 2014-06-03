package qa.qcri.aidr.trainer.pybossa.service;

import qa.qcri.aidr.trainer.pybossa.entity.ReportTemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/22/13
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReportTemplateService {
    void saveReportItem(ReportTemplate reportTemplate);
    List<ReportTemplate> getReportTemplateByClientApp(Long clientAppID, Integer status);
    void updateReportItem(ReportTemplate reportTemplate);
    List<ReportTemplate> getReportTemplateSearchByTwittID(String field, String value);
}
