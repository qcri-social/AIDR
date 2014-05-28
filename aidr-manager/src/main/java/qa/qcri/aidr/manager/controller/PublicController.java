package qa.qcri.aidr.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import qa.qcri.aidr.manager.dto.AidrCollectionTotalDTO;
import qa.qcri.aidr.manager.dto.TaggerCrisisType;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.service.CollectionLogService;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.util.CollectionStatus;
import qa.qcri.aidr.manager.util.JsonDataValidator;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("public/collection")
public class PublicController extends BaseController{
    private Logger logger = Logger.getLogger(PublicController.class);

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private TaggerService taggerService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


    @RequestMapping(value = "/findAll.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>  findAll(@RequestParam Integer start, @RequestParam Integer limit,  @RequestParam Enum statusValue,
                                             @DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
        //logger.info("public findall is called");
        start = (start != null) ? start : 0;
        limit = (limit != null) ? limit : 50;

        try {

            List<AidrCollection> data = collectionService.findAllForPublic(start, limit, statusValue);
           // logger.info("data size : " + data.size());
            return getUIWrapper(data, true);

        } catch (Exception e) {
            e.printStackTrace();
           return getUIWrapper(false);
        }

        //return getUIWrapper(false);
    }

     @RequestMapping(value = "/findAllRunning.action", method = RequestMethod.GET)
     @ResponseBody
     public Map<String,Object>  findAllRunning(@RequestParam Integer start, @RequestParam Integer limit,
                                               @DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
       // logger.info("public findAllRunning is called");
        start = (start != null) ? start : 0;
        limit = (limit != null) ? limit : 50;
        Integer count = 0;
        List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();

        try {
        //    logger.info("*************************************************  CollectionStatus.RUNNING ****************************");
            List<AidrCollection> data = collectionService.findAllForPublic(start, limit, CollectionStatus.RUNNING);
       //     logger.info("data size : " + data.size());

            for (AidrCollection collection : data) {
                String taggingOutPut = taggerService.loadLatestTweetsWithCount(collection.getCode(), 1);
                //System.out.println("taggingOutPut : " + taggingOutPut);
                if(!JsonDataValidator.isEmptySON(taggingOutPut))  {
                    AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, true);
                    dtoList.add(dto);
                    count = count +1;
                }
            }

            return getUIWrapper(dtoList, count.longValue());

        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false);
        }

        //return getUIWrapper(false);
    }

    @RequestMapping(value = "/findAllRunningWithNoOutput.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>  findAllRunningWithNoOutput(@RequestParam Integer start, @RequestParam Integer limit,
                                              @DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
        // logger.info("public findAllRunning is called");
        start = (start != null) ? start : 0;
        limit = (limit != null) ? limit : 50;
        Integer count = 0;
        List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();

        try {
            //    logger.info("*************************************************  CollectionStatus.RUNNING ****************************");
            List<AidrCollection> data = collectionService.findAllForPublic(start, limit, CollectionStatus.RUNNING);
            //     logger.info("data size : " + data.size());
            //count = collectionService.getPublicCollectionsCount(CollectionStatus.RUNNING);
            for (AidrCollection collection : data) {
                String taggingOutPut = taggerService.loadLatestTweetsWithCount(collection.getCode(), 1);
                //System.out.println("taggingOutPut : " + taggingOutPut);
                if(JsonDataValidator.isEmptySON(taggingOutPut))  {
                    AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, false);
                    dtoList.add(dto);
                    count = count +1;
                }
            }

            return getUIWrapper(dtoList, count.longValue());

        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false);
        }

        //return getUIWrapper(false);
    }

    @RequestMapping(value = "/findAllStoped.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>  findAllStop(@RequestParam Integer start, @RequestParam Integer limit,
                                              @DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
       // logger.info("public findAllStop is called");
        start = (start != null) ? start : 0;
        limit = (limit != null) ? limit : 50;
        Integer count = 0;
        List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();
        try {
           // logger.info("*************************************************  CollectionStatus.STOPPED ****************************");
            List<AidrCollection> data = collectionService.findAllForPublic(start, limit, CollectionStatus.STOPPED);
            count = collectionService.getPublicCollectionsCount(CollectionStatus.STOPPED);
          //  logger.info("data size : " + data.size());
            boolean hasTagggerOutput;
            for (AidrCollection collection : data) {
                String taggingOutPut = taggerService.loadLatestTweetsWithCount(collection.getCode(), 1);
                //System.out.println("taggingOutPut : " + taggingOutPut);
                if(JsonDataValidator.isEmptySON(taggingOutPut))  {
                    hasTagggerOutput = false;
                }
                else{
                    hasTagggerOutput = true;
                }

                AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, hasTagggerOutput);
                dtoList.add(dto);
            }

            return getUIWrapper(dtoList, count.longValue());

        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false);
        }

        //return getUIWrapper(false);
    }

    private AidrCollectionTotalDTO convertAidrCollectionToDTO(AidrCollection collection, boolean hasTaggerOutput){
        if (collection == null){
            return null;
        }

        AidrCollectionTotalDTO dto = new AidrCollectionTotalDTO();

        dto.setId(collection.getId());
        dto.setCode(collection.getCode());
        dto.setName(collection.getName());
        dto.setTarget(collection.getTarget());

        UserEntity user = collection.getUser();
        user.setRoles(null);
        dto.setUser(user);

        if (collection.getCount() != null) {
            dto.setCount(collection.getCount());
        } else {
            dto.setCount(0);
        }
        dto.setStatus(collection.getStatus());
        dto.setTrack(collection.getTrack());
        dto.setFollow(collection.getFollow());
        dto.setGeo(collection.getGeo());
        dto.setLangFilters(collection.getLangFilters());
        dto.setStartDate(collection.getStartDate());
        dto.setEndDate(collection.getEndDate());
        dto.setCreatedDate(collection.getCreatedDate());
        dto.setLastDocument(collection.getLastDocument());
        dto.setDurationHours(collection.getDurationHours());
        dto.setPubliclyListed(collection.getPubliclyListed());
        dto.setCrisisType(collection.getCrisisType());
        dto.setHasTaggerOutput(hasTaggerOutput);


        if(collection.getCrisisType() != null){
            dto.setCrisisTypeName(getCrisisTypeName(collection.getCrisisType()));
        }

        List<UserEntity> managers = collection.getManagers();
        for (UserEntity manager : managers) {
            manager.setRoles(null);
        }
        dto.setManagers(managers);

        return dto;
    }

    private String getCrisisTypeName(int typeID){
        String name = "Not specified";
       // System.out.println("getCrisisTypeName: " + typeID);
        try {
            List<TaggerCrisisType> crisisTypes = taggerService.getAllCrisisTypes();

            for (TaggerCrisisType cType : crisisTypes) {
                if(cType.getCrisisTypeID() == typeID) {
                    name = cType.getName();
                }
            }

        } catch (AidrException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return name;
    }

}
