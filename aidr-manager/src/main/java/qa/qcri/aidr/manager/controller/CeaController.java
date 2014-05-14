package qa.qcri.aidr.manager.controller;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.util.CollectionStatus;
import qa.qcri.aidr.manager.util.JsonDataValidator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("cea/collection")
public class CeaController extends BaseController{

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private CollectionService collectionService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "/start.action", method = RequestMethod.POST)
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseBody
    public Map<String,Object> saveStart(@RequestBody final String jsonString) throws Exception {
        //
        logger.info("saveStart CeaController  : "+jsonString);
        try{
            if(jsonString == null){
                return getUIWrapper(false);
            }

            if(JsonDataValidator.isValidCeaJSON(jsonString)){
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(jsonString);

                JSONObject jsonObject = (JSONObject) obj;
                String token = (String)jsonObject.get("token");

                if(collectionService.isValidToken(token)){
                    String code = (String) jsonObject.get("code");
                    String geo = (String) jsonObject.get("geo");
                    long defaultHours = (Long)jsonObject.get("durationInHours") ;

                    JSONArray account = (JSONArray) jsonObject.get("account");
                    UserEntity userEntity= userService.getAvailableUser(account) ;
                    if(userEntity != null){
                        logger.info("userEntity CeaController  : "+userEntity.getUserName());
                        List<AidrCollection> collectionList = collectionService.geAllCollectionByUser(userEntity.getId()) ;

                        if(collectionList != null && collectionList.size() > 0){
                            for(int i=0; i < collectionList.size() ; i++){
                                AidrCollection aCol = collectionList.get(i);
                                if(aCol.getStatus().equals(CollectionStatus.INITIALIZING) || aCol.getStatus().equals(CollectionStatus.RUNNING) ){
                                    logger.info("collectionService.stop : " + aCol.getId());
                                    try{
                                        collectionService.stop(aCol.getId());
                                    }
                                    catch(Exception e){
                                        logger.error("Error while stop running collection", e);
                                        return getUIWrapper(e.getMessage(), false);
                                    }
                                }
                            }
                        }

                        AidrCollection collection = new AidrCollection();
                        collection.setUser(userEntity);
                        collection.setStatus(CollectionStatus.NOT_RUNNING);
                        collection.setPubliclyListed(true);
                        Calendar now = Calendar.getInstance();
                        collection.setCreatedDate(now.getTime());

                        String name = code;
                        code = code+ now.getTime().hashCode();
                        logger.info("updated code : " + code);
                        collection.setCode(code);
                        collection.setGeo(geo);
                        collection.setName(name);
                        collection.setDurationHours((int)defaultHours);
                        collection.setLangFilters("");
                        List<UserEntity> managers = new ArrayList<UserEntity>();
                        managers.add(userEntity);
                        collection.setManagers(managers);

                        try{
                            collectionService.create(collection);
                            collectionService.start(collection.getId());

                            return getUIWrapper("successful", true, Long.parseLong("1"), "successful");
                        }
                        catch(Exception e){
                            logger.error("failing to create or start collection", e);
                            return getUIWrapper(e.getMessage(), false);
                        }
                    }
                    else{
                        return getUIWrapper("bad user account info", false);
                    }
                }
                else{
                    return getUIWrapper("Invalid token", false);
                }
            }
            return getUIWrapper("bad json", false);

        }catch(Exception e){
            logger.error("Error while saveStart AidrCollection. unknown issues....", e);
            return getUIWrapper(e.getMessage(), false);
        }
    }


}
