package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.ReportTemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/22/13
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReportTemplateDao  extends AbstractDao<ReportTemplate, String>{
    void saveReportItem(ReportTemplate reportTemplate);
    void updateReportItem(ReportTemplate reportTemplate);
    List<ReportTemplate> getReportTemplateByClientApp(Long clientAppID, Integer status);
}
