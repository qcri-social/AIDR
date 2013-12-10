package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.dao.ReportTemplateDao;
import qa.qcri.aidr.trainer.pybossa.entity.ReportTemplate;
import qa.qcri.aidr.trainer.pybossa.service.ReportTemplateService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/22/13
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("reportTemplateService")
@Transactional(readOnly = false)
public class ReportTemplateServiceImpl implements ReportTemplateService {

    @Autowired
    private ReportTemplateDao reportTemplateDao;

    @Override
    @Transactional(readOnly = false)
    public void saveReportItem(ReportTemplate reportTemplate) {
        reportTemplateDao.saveReportItem(reportTemplate);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ReportTemplate> getReportTemplateByClientApp(Long clientAppID, Integer status) {
        return reportTemplateDao.getReportTemplateByClientApp(clientAppID, status);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = false)
    public void updateReportItem(ReportTemplate reportTemplate) {
        reportTemplateDao.updateReportItem(reportTemplate);
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
