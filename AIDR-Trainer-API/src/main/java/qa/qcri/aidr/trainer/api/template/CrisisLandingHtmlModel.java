package qa.qcri.aidr.trainer.api.template;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/2/13
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrisisLandingHtmlModel {

    public CrisisLandingHtmlModel(String crisisCode, String crisisName, List<CrisisApplicationListModel> crisisApplicationListModelList){
        this.crisisApplicationListModelList = crisisApplicationListModelList;
        this.crisisCode = crisisCode;
        this.crisisName = crisisName;

    }


    public String getCrisisName() {
        return crisisName;
    }

    public void setCrisisName(String crisisName) {
        this.crisisName = crisisName;
    }

    public String getCrisisCode() {
        return crisisCode;
    }

    public void setCrisisCode(String crisisCode) {
        this.crisisCode = crisisCode;
    }

    public List<CrisisApplicationListModel> getCrisisApplicationListModelList() {
        return crisisApplicationListModelList;
    }

    public void setCrisisApplicationListModelList(List<CrisisApplicationListModel> crisisApplicationListModelList) {
        this.crisisApplicationListModelList = crisisApplicationListModelList;
    }


    private List<CrisisApplicationListModel> crisisApplicationListModelList;
    private String crisisName;
    private String crisisCode;


}
