package qa.qcri.aidr.trainer.api.template;

import qa.qcri.aidr.trainer.api.store.CodeLookUp;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/16/14
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientAppDeploymentModel {

    public Long deploymentID;
    public Long clientAppID;
    public String choices;
    public String clientAppName;
    public int  appType;
    public String appTypeName;

    public ClientAppDeploymentModel(Long deploymentID, Long clientAppID, String choices, String clientAppName, int appType) {
        this.deploymentID = deploymentID;
        this.clientAppID = clientAppID;
        this.choices = choices;
        this.clientAppName = clientAppName;
        this.appType = appType;
        this.appTypeName = setAppTypeName(appType);
    }

    public Long getDeploymentID() {
        return deploymentID;
    }

    public void setDeploymentID(Long deploymentID) {
        this.deploymentID = deploymentID;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public Long getClientAppID() {
        return clientAppID;
    }

    public void setClientAppID(Long clientAppID) {
        this.clientAppID = clientAppID;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getAppTypeName() {
        return appTypeName;
    }

    public String setAppTypeName(int appType) {
        if(appType == CodeLookUp.APP_MULTIPLE_CHOICE) {
            this.appTypeName = CodeLookUp.APP_MULTIPLE_CHOICE_NAME;
        }

        if(appType == CodeLookUp.APP_IMAGE) {
            this.appTypeName = CodeLookUp.APP_IMAGE_NAME;
        }

        if(appType == CodeLookUp.APP_VIDEO) {
            this.appTypeName = CodeLookUp.APP_VIDEO_NAME;
        }

        if(appType == CodeLookUp.APP_MAP) {
            this.appTypeName = CodeLookUp.APP_MAP_NAME;
        }

        if(appType == CodeLookUp.APP_AERIAL) {
            this.appTypeName = CodeLookUp.APP_AERIAL_NAME;
        }

        return this.appTypeName;

    }
}
