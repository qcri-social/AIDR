package qa.qcri.aidr.trainer.pybossa.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.pybossa.dao.ReportTemplateDao;
import qa.qcri.aidr.trainer.pybossa.entity.ReportTemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/22/13
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ReportTemplateDaoImpl extends AbstractDaoImpl<ReportTemplate, String> implements ReportTemplateDao {

    protected ReportTemplateDaoImpl(){
        super(ReportTemplate.class);
    }

    @Override
    public void saveReportItem(ReportTemplate reportTemplate) {
        List<ReportTemplate> templateList =  findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("taskQueueID",reportTemplate.getTaskQueueID()))
                .add(Restrictions.eq("answer", reportTemplate.getAnswer())));

        if(templateList.size() == 0) {
            save(reportTemplate);
        }
    }

    @Override
    public void updateReportItem(ReportTemplate reportTemplate) {
        //To change body of implemented methods use File | Settings | File Templates.
        ReportTemplate reportItem = findByCriterionID(Restrictions.eq("reportTemplateID",reportTemplate.getReportTemplateID()));
        if(reportItem != null){
            reportItem.setStatus(reportTemplate.getStatus());
            saveOrUpdate(reportItem);
        }
    }


    @Override
    public List<ReportTemplate> getReportTemplateByClientApp(Long clientAppID, Integer status) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("clientAppID",clientAppID))
                .add(Restrictions.eq("status", status)));

    }
}
