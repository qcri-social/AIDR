package qa.qcri.aidr.manager.controller;

import qa.qcri.aidr.manager.dto.CollectionLogDataResponse;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;
import qa.qcri.aidr.manager.service.CollectionLogService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("protected/collection-log")
public class CollectionLogController extends BaseController {

    private Logger logger = Logger.getLogger(CollectionLogController.class);

    @Autowired
    private CollectionLogService collectionLogService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "/save.action", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> save(AidrCollectionLog collectionLog) throws Exception {
        logger.info("Save AidrCollectionLog to Database for collection with ID: " + collectionLog.getCollectionID());
        try {
            collectionLogService.create(collectionLog);
            return getUIWrapper(true);
        } catch (Exception e) {
            logger.error("Error while saving AidrCollectionLog Info to database", e);
            return getUIWrapper(false);
        }
    }

    @RequestMapping(value = "/delete.action", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> delete(AidrCollectionLog collectionLog) throws Exception {
        logger.info("Deleting AidrCollectionLog Info from Database for collection with ID: " + collectionLog.getCollectionID());
        try {
            collectionLogService.delete(collectionLog);
            return getUIWrapper(true);
        } catch (Exception e) {
            logger.error("Error while deleting AIDRCollectionLog Info from database", e);
            return getUIWrapper(false);
        }
    }

    @RequestMapping(value = "/update.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(AidrCollectionLog collectionLog) throws Exception {
        logger.info("Updating AidrCollectionLog into Database for collection with ID: " + collectionLog.getCollectionID());
        try {
            AidrCollectionLog dbCollection = collectionLogService.findById(collectionLog.getId());
            collectionLog.setStartDate(dbCollection.getStartDate());
            collectionLog.setCollectionID(dbCollection.getCollectionID());
            collectionLogService.update(collectionLog);
            return getUIWrapper(true);
        } catch (Exception e) {
            logger.error("Error while Updating AidrCollectionLog  Info into database", e);
            return getUIWrapper(false);
        }
    }

    @RequestMapping(value = "/findById.action", method = RequestMethod.GET)
    @ResponseBody
    public AidrCollectionLog findById(Integer id) throws Exception {
        logger.info("Fetch AidrCollectionLog for Id  " + id);
        return collectionLogService.findById(id);
    }

    @RequestMapping(value = "/findAll.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findAll(@RequestParam Integer start, @RequestParam Integer limit) throws Exception {
        start = (start != null) ? start : 0;
        limit = (limit != null) ? limit : 50;
        CollectionLogDataResponse dataResponse = collectionLogService.findAll(start, limit);
        return getUIWrapper(dataResponse.getData(), dataResponse.getTotal());
    }

    @RequestMapping(value = "/findAllForCollection.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findAllForCollection(@RequestParam Integer start, @RequestParam Integer limit, @RequestParam Integer id) throws Exception {
        if (id == null) {
            logger.error("Error while Getting all AidrCollectionLogs for Collection. Collection ID is empty");
            return getUIWrapper(false);
        }
        start = (start != null) ? start : 0;
        limit = (limit != null) ? limit : 50;
        CollectionLogDataResponse dataResponse = collectionLogService.findAllForCollection(start, limit, id);
        return getUIWrapper(dataResponse.getData(), dataResponse.getTotal());
    }

}