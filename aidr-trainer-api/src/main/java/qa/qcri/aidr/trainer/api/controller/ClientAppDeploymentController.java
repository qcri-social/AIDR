package qa.qcri.aidr.trainer.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.trainer.api.entity.ClientApp;
import qa.qcri.aidr.trainer.api.entity.ClientAppAnswer;
import qa.qcri.aidr.trainer.api.entity.ClientAppDeployment;
import qa.qcri.aidr.trainer.api.service.ClientAppAnswerService;
import qa.qcri.aidr.trainer.api.service.ClientAppDeploymentService;
import qa.qcri.aidr.trainer.api.service.ClientAppService;
import qa.qcri.aidr.trainer.api.template.ClientAppDeploymentModel;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/16/14
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/deployment")
@RestController
public class ClientAppDeploymentController {

    @Autowired
    ClientAppDeploymentService clientAppDeploymentService;

    @Autowired
    ClientAppAnswerService appAnswerService;

    @Autowired
    ClientAppService appService;


    @RequestMapping("/active/type/{typeID}")
    public ClientAppDeploymentModel getActiveByType(@PathVariable("typeID") Integer typeID){
        ClientAppDeployment deploy=  clientAppDeploymentService.getActiveDeploymentForAppType(typeID);
        if(deploy != null){
            ClientAppAnswer cAns = appAnswerService.getClientAppAnswer(deploy.getClientAppID()) ;
            ClientApp cApp = appService.findClientAppByID("clientAppID", deploy.getClientAppID());
            if(cAns != null && cApp != null){
                ClientAppDeploymentModel aModel = new ClientAppDeploymentModel(deploy.getDeploymentID(), deploy.getClientAppID(), cAns.getAnswer(), cApp.getName(), cApp.getAppType(), cApp.getPlatformAppID() );
                return aModel;
            }
        }
        return null;
    }

    @RequestMapping("/active")
    public List<ClientAppDeploymentModel> getActive(){
        List<ClientAppDeployment> cDeploys = clientAppDeploymentService.getActiveDeployment();

        if(cDeploys != null && cDeploys.size() > 0){
            List<ClientAppDeploymentModel> models = new ArrayList<ClientAppDeploymentModel>();
            for(ClientAppDeployment c : cDeploys) {
                ClientAppAnswer cAns = appAnswerService.getClientAppAnswer(c.getClientAppID()) ;
                ClientApp cApp = appService.findClientAppByID("clientAppID", c.getClientAppID());
                if(cAns != null && cApp != null){
                    ClientAppDeploymentModel aModel = new ClientAppDeploymentModel(c.getDeploymentID(), c.getClientAppID(), cAns.getAnswer(), cApp.getName(), cApp.getAppType(), cApp.getPlatformAppID());
                    models.add(aModel)  ;
                }
            }
            return models;
        }

        return null ;
    }

    @RequestMapping("/active/mobile")
    public List<ClientAppDeploymentModel> getMobileActive(){
        List<ClientAppDeployment> cDeploys = clientAppDeploymentService.getMobileActiveDeployment();

        if(cDeploys != null && cDeploys.size() > 0){
            List<ClientAppDeploymentModel> models = new ArrayList<ClientAppDeploymentModel>();
            for(ClientAppDeployment c : cDeploys) {
                ClientAppAnswer cAns = appAnswerService.getClientAppAnswer(c.getClientAppID()) ;
                ClientApp cApp = appService.findClientAppByID("clientAppID", c.getClientAppID());
                if(cAns != null && cApp != null){
                    ClientAppDeploymentModel aModel = new ClientAppDeploymentModel(c.getDeploymentID(), c.getClientAppID(), cAns.getAnswer(), cApp.getName(), cApp.getAppType(), cApp.getPlatformAppID());
                    models.add(aModel)  ;
                }
            }
            return models;
        }

        return null ;
    }

}
