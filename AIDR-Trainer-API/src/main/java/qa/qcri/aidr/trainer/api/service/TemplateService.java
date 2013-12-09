package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.template.CrisisApplicationListModel;
import qa.qcri.aidr.trainer.api.template.CrisisLandingHtmlModel;
import qa.qcri.aidr.trainer.api.template.CrisisLandingStatusModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/27/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TemplateService {

    List<CrisisApplicationListModel> getApplicationListHtmlByCrisisID(Long crisisID);
    List<CrisisApplicationListModel> getApplicationListHtmlByCrisisCode(String crisisCode);
    CrisisLandingHtmlModel getCrisisLandingHtmlByCrisisCode(String crisisCode);
    CrisisLandingHtmlModel getCrisisLandingHtmlByCrisisID(Long crisisID);
    String getCrisisLandingJSONPByCrisisID(Long crisisID);
    String getCrisisLandingJSONPByCrisisCode(String crisisCode);
    CrisisLandingStatusModel getCrisisLandingStatusByCrisisCode(String crisisCode);
}
