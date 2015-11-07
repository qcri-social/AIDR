package qa.qcri.aidr.data.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.data.model.CollectionSummaryInfo;
import qa.qcri.aidr.data.service.CollectionSummaryService;

@Controller
@RequestMapping("/dashboard")
public class CollectionSummaryController {

	@Autowired
    protected CollectionSummaryService collectionSummaryService;
    
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(CollectionSummaryController.class);
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<CollectionSummaryInfo> list(){
    	List<CollectionSummaryInfo> list=collectionSummaryService.fetchAllCollections();
        return list;
    }
}
