package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import qa.qcri.aidr.trainer.pybossa.dao.ReportTemplateDao;
import qa.qcri.aidr.trainer.pybossa.entity.ReportTemplate;
import qa.qcri.aidr.trainer.pybossa.service.ReportTemplateService;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;

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

	private static Logger logger = Logger.getLogger(ReportTemplateServiceImpl.class);

	
    @Autowired
    private ReportTemplateDao reportTemplateDao;

    @Override
    @Transactional(readOnly = false, propagation= Propagation.REQUIRES_NEW)
    public void saveReportItem(ReportTemplate reportTemplate) {
        System.out.println("saveReportItem is called");
        try{
            if(isNumeric(reportTemplate.getTweetID()) && reportTemplate.getUrl().length() < 300 && reportTemplate.getAuthor().length() < 100){
                reportTemplateDao.saveReportItem(reportTemplate);
            }
        }
        catch(Exception ex){
            logger.error("saveReportItem exception");
            logger.error(ex.getMessage());
            System.out.println("saveReportItem is called error : " + ex.toString());
            throw new RuntimeException(ex.getMessage());
        }

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ReportTemplate> getReportTemplateWithUniqueKey(String uniqueKey) {
        return reportTemplateDao.getReportTemplateWithUniqueKey("status", StatusCodeType.TEMPLATE_IS_READY_FOR_EXPORT, uniqueKey);  //To change body of implemented methods use File | Settings | File Templates.
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

    @Override
    public List<ReportTemplate> getReportTemplateSearchByTwittID(String field, String value) {
        return reportTemplateDao.getReportTemplateSearchBy(field, value);  //To change body of implemented methods use File | Settings | File Templates.
    }

    private boolean isNumeric(String data){
        boolean returnValue = false;
        try{
            Long tweetID = Long.parseLong(data);
            returnValue = true;
        }
        catch(Exception e){
            logger.error("isNumeric exception on TweetID: " + data);
            logger.error(e.getMessage());
        }

        return returnValue;
    }
}
