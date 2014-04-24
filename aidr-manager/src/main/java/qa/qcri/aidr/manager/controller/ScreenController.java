package qa.qcri.aidr.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import qa.qcri.aidr.manager.dto.TaggerCrisis;
import qa.qcri.aidr.manager.dto.TaggerModel;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.TaggerService;

import java.util.List;
import java.util.Map;


@Controller
public class ScreenController extends BaseController{

    @Autowired
    private CollectionService collectionService;
    @Autowired
    private TaggerService taggerService;

	@RequestMapping("protected/home")
	public ModelAndView home() throws Exception {
        String userName = getAuthenticatedUserName();

        ModelAndView model = new ModelAndView("home");
        model.addObject("userName", userName);
        return model;
	}

	@RequestMapping("signin")
	public String signin(Map<String, String> model) throws Exception {
		return "signin";
	}

    @RequestMapping("protected/access-error")
    public ModelAndView accessError() throws Exception {
        return new ModelAndView("access-error");
    }

    private boolean isHasPermissionForCollection(String code) throws Exception{
        UserEntity user = getAuthenticatedUser();
        if (user == null){
            return false;
        }

//        current user is Admin
        if (userService.isUserAdmin(user)) {
            return true;
        }

        AidrCollection collection = collectionService.findByCode(code);
        if (collection == null){
            return false;
        }

//        current user is a owner of the collection
        if(user.getUserName().equals(collection.getUser().getUserName())){
            return true;
        }

//        current user is in managers list of the collection
        if (userService.isUserInCollectionManagersList(user, collection)){
            return true;
        }
        return false;
    }

    @RequestMapping("protected/{code}/collection-details")
    public ModelAndView collectionDetails(@PathVariable(value="code") String code) throws Exception {
        if (!isHasPermissionForCollection(code)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        String userName = getAuthenticatedUserName();
        AidrCollection collection = collectionService.findByCode(code);

        ModelAndView model = new ModelAndView("collection-details");
        model.addObject("id", collection.getId());
        model.addObject("collectionCode", code);
        model.addObject("userName", userName);
        return model;
    }

    @RequestMapping("protected/collection-create")
    public String collectionCreate(Map<String, String> model) throws Exception {
        return "collection-create";
    }

    @RequestMapping("protected/tagger-home")
    public String taggerHome(Map<String, String> model) throws Exception {
        return "tagger/home";
    }

    @RequestMapping("protected/{code}/tagger-collection-details")
    public ModelAndView taggerCollectionDetails(@PathVariable(value="code") String code) throws Exception {
        if (!isHasPermissionForCollection(code)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        TaggerCrisis crisis = taggerService.getCrisesByCode(code);

        Integer crisisId = 0;
        String crisisName = "";
        Integer crisisTypeId = 0;
        if (crisis != null && crisis.getCrisisID() != null && crisis.getName() != null){
            crisisId = crisis.getCrisisID();
            crisisName = crisis.getName();
            if (crisis.getCrisisType() != null && crisis.getCrisisType().getCrisisTypeID() != null){
                crisisTypeId = crisis.getCrisisType().getCrisisTypeID();
            }
        }

        ModelAndView model = new ModelAndView("tagger/tagger-collection-details");
        model.addObject("crisisId", crisisId);
        model.addObject("name", crisisName);
        model.addObject("crisisTypeId", crisisTypeId);
        model.addObject("code", code);
        return model;
    }

    @RequestMapping("protected/{code}/predict-new-attribute")
    public ModelAndView predictNewAttribute(@PathVariable(value="code") String code) throws Exception {
        if (!isHasPermissionForCollection(code)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        TaggerCrisis crisis = taggerService.getCrisesByCode(code);

        Integer crisisId = 0;
        String crisisName = "";
        if (crisis != null && crisis.getCrisisID() != null && crisis.getName() != null){
            crisisId = crisis.getCrisisID();
            crisisName = crisis.getName();
        }

        ModelAndView model = new ModelAndView("tagger/predict-new-attribute");
        model.addObject("crisisId", crisisId);
        model.addObject("name", crisisName);
        model.addObject("code", code);
        return model;
    }

    @RequestMapping("protected/{id}/attribute-details")
    public ModelAndView attributeDetails(@PathVariable(value="id") Integer id) throws Exception {
        ModelAndView model = new ModelAndView("tagger/attribute-details");
        Integer taggerUserId = 0;
        try {
            String userName = getAuthenticatedUserName();
            taggerUserId = taggerService.isUserExistsByUsername(userName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addObject("id", id);
        model.addObject("userId", taggerUserId);
        return model;
    }

    @RequestMapping("protected/{code}/{id}/model-details")
    public ModelAndView modelDetails(@PathVariable(value="code") String code, @PathVariable(value="id") Integer modelId) throws Exception {
        if (!isHasPermissionForCollection(code)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        TaggerCrisis crisis = taggerService.getCrisesByCode(code);

        Integer crisisId = 0;
        Integer modelFamilyId = 0;
        Integer attributeId = 0;
        String crisisName = "";
        String modelName = "";
        double modelAuc = 0;
        if (crisis != null && crisis.getCrisisID() != null && crisis.getName() != null){
            crisisId = crisis.getCrisisID();
            crisisName = crisis.getName();
        }

        List<TaggerModel> modelsForCrisis = taggerService.getModelsForCrisis(crisisId);
        for (TaggerModel model : modelsForCrisis) {
            if (modelId.equals(model.getModelID())){
                modelName = model.getAttribute();
                if (model.getModelFamilyID() != null) {
                    modelFamilyId = model.getModelFamilyID();
                }
                modelAuc = model.getAuc();
                attributeId = model.getAttributeID();
            }
        }

        Integer taggerUserId = 0;
        try {
            String userName = getAuthenticatedUserName();
            taggerUserId = taggerService.isUserExistsByUsername(userName);
            if(taggerUserId == null){
                taggerUserId = 0;
            }


        } catch (Exception e) {
            System.out.println("e : " + e);
            e.printStackTrace();
        }

        ModelAndView model = new ModelAndView("tagger/model-details");
        model.addObject("crisisId", crisisId);
        model.addObject("crisisName", crisisName);
        model.addObject("modelName", modelName);
        model.addObject("modelId", modelId);
        model.addObject("modelAuc", modelAuc);
        model.addObject("modelFamilyId", modelFamilyId);
        model.addObject("code", code);
        model.addObject("userId", taggerUserId);
        model.addObject("attributeId", attributeId);

        return model;
    }

    @RequestMapping("protected/{code}/new-custom-attribute")
    public ModelAndView newCustomAttribute(@PathVariable(value="code") String code) throws Exception {
        if (!isHasPermissionForCollection(code)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        TaggerCrisis crisis = taggerService.getCrisesByCode(code);
        Integer crisisId = 0;
        String crisisName = "";

        if (crisis != null && crisis.getCrisisID() != null && crisis.getName() != null){
            crisisId = crisis.getCrisisID();
            crisisName = crisis.getName();
        }

        ModelAndView model = new ModelAndView("tagger/new-custom-attribute");
        model.addObject("code", code);
        model.addObject("crisisId", crisisId);
        model.addObject("crisisName", crisisName);

        return model;
    }

    @RequestMapping("protected/{code}/{modelId}/{modelFamilyId}/{attributeID}/training-data")
    public ModelAndView trainingData(@PathVariable(value="code") String code,
                                     @PathVariable(value="modelId") Integer modelId,
                                     @PathVariable(value="modelFamilyId") Integer modelFamilyId,
                                     @PathVariable(value="attributeID") Integer attributeID) throws Exception {
        if (!isHasPermissionForCollection(code)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        TaggerCrisis crisis = taggerService.getCrisesByCode(code);

        Integer crisisId = 0;
        String crisisName = "";
        String modelName = "";
        double modelAuc = 0;
        long trainingExamples = 0;
        if (crisis != null && crisis.getCrisisID() != null && crisis.getName() != null){
            crisisId = crisis.getCrisisID();
            crisisName = crisis.getName();

        }

        List<TaggerModel> modelsForCrisis = taggerService.getModelsForCrisis(crisisId);
        for (TaggerModel model : modelsForCrisis) {
            if (attributeID.equals(model.getAttributeID())){
                modelName = model.getAttribute();
                trainingExamples = model.getTrainingExamples();
                modelAuc = model.getAuc();
            }
        }

        ModelAndView model = new ModelAndView("tagger/training-data");
        model.addObject("crisisId", crisisId);
        model.addObject("crisisName", crisisName);
        model.addObject("modelName", modelName);
        model.addObject("modelId", modelId);
        model.addObject("modelFamilyId", modelFamilyId);
        model.addObject("attributeID", attributeID);
        model.addObject("code", code);
        model.addObject("trainingExamples", trainingExamples);
        model.addObject("modelAuc", modelAuc);
        return model;
    }

    @RequestMapping("protected/{code}/{modelId}/{modelFamilyId}/{nominalAttributeId}/training-examples")
    public ModelAndView trainingExamples(@PathVariable(value="code") String code,
                                         @PathVariable(value="modelId") Integer modelId,
                                         @PathVariable(value="modelFamilyId") Integer modelFamilyId,
                                         @PathVariable(value="nominalAttributeId") Integer nominalAttributeId) throws Exception {
        if (!isHasPermissionForCollection(code)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        TaggerCrisis crisis = taggerService.getCrisesByCode(code);

        Integer crisisId = 0;
        String crisisName = "";
        String modelName = "";
        if (crisis != null && crisis.getCrisisID() != null && crisis.getName() != null){
            crisisId = crisis.getCrisisID();
            crisisName = crisis.getName();
        }

        List<TaggerModel> modelsForCrisis = taggerService.getModelsForCrisis(crisisId);
        for (TaggerModel model : modelsForCrisis) {
            if (nominalAttributeId.equals(model.getAttributeID())){
                modelName = model.getAttribute();
            }
        }

        ModelAndView model = new ModelAndView("tagger/training-examples");
        model.addObject("code", code);
        model.addObject("crisisId", crisisId);
        model.addObject("crisisName", crisisName);
        model.addObject("modelName", modelName);
        model.addObject("modelId", modelId);
        model.addObject("modelFamilyId", modelFamilyId);
        model.addObject("nominalAttributeId", nominalAttributeId);

        return model;
    }

    @RequestMapping("protected/administration/admin-console")
    public ModelAndView adminConsole(Map<String, String> model) throws Exception {
        UserEntity user = getAuthenticatedUser();
        if (!userService.isUserAdmin(user)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        return new ModelAndView( "administration/admin-console");
    }

    @RequestMapping("protected/administration/admin-health")
    public ModelAndView adminHealth(Map<String, String> model) throws Exception {
        UserEntity user = getAuthenticatedUser();
        if (!userService.isUserAdmin(user)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        return new ModelAndView("administration/health");
    }

    @RequestMapping("protected/{code}/interactive-view-download")
    public ModelAndView interactiveViewDownload(@PathVariable(value="code") String code) throws Exception {
        if (!isHasPermissionForCollection(code)){
            return new ModelAndView("redirect:/protected/access-error");
        }

        String userName = getAuthenticatedUserName();

        TaggerCrisis crisis = null;
        AidrCollection collection = null;
        try {
            crisis = taggerService.getCrisesByCode(code);
            collection = collectionService.findByCode(code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Integer crisisId = 0;
        String crisisName = "";
        if (crisis != null && crisis.getCrisisID() != null && crisis.getName() != null){
            crisisId = crisis.getCrisisID();
            crisisName = crisis.getName();
        }

        Integer collectionId = 0;
        if (collection != null && collection.getId() != null){
            collectionId = collection.getId();
        }

        ModelAndView model = new ModelAndView("tagger/interactive-view-download");
        model.addObject("collectionId", collectionId);
        model.addObject("crisisId", crisisId);
        model.addObject("crisisName", crisisName);
        model.addObject("code", code);
        model.addObject("userName", userName);
        return model;
    }

}
