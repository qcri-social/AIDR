package qa.qcri.aidr.trainer.pybossa.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.trainer.pybossa.dao.ReportTemplateDao;
import qa.qcri.aidr.trainer.pybossa.entity.ReportTemplate;

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
        	if(StringUtils.isEmpty(reportTemplate.getCreated())){
        		Date date = new Date();
        		reportTemplate.setCreated(date.toString());
        	}
            save(reportTemplate);
        }
    }

    @Override
    public void updateReportItem(ReportTemplate reportTemplate) {
        //To change body of implemented methods use File | Settings | File Templates.
        ReportTemplate reportItem = findByCriterionID(Restrictions.eq("reportTemplateID", reportTemplate.getReportTemplateID()));
        if(reportItem != null){
        	if(StringUtils.isEmpty(reportTemplate.getCreated())){
        		Date date = new Date();
        		reportTemplate.setCreated(date.toString());
        	}
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

    @Override
    public List<ReportTemplate> getReportTemplateSearchBy(String field, String value) {
        return findByCriteria(Restrictions.eq(field, value));

    }

    @Override
    public List<ReportTemplate> getReportTemplateWithUniqueKey(String field, Integer value, String uniqueKey ) {
        return findUniqueByCriteria(Restrictions.eq(field, value), uniqueKey);

    }
}
