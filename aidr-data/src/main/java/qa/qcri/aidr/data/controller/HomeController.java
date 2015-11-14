package qa.qcri.aidr.data.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import qa.qcri.aidr.data.model.CollectionSummaryInfo;
import qa.qcri.aidr.data.service.CollectionSummaryService;

@Controller
public class HomeController {

	@Autowired
    protected CollectionSummaryService collectionSummaryService;
    
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(HomeController.class);
    
    @RequestMapping(value = "/dashboard/list")
    @ResponseBody
    public List<CollectionSummaryInfo> list(){
    	List<CollectionSummaryInfo> list = collectionSummaryService.fetchAllCollections();
        return list;
    }
    
    @ResponseBody
    @RequestMapping(value = "/dashboard")
    public ModelAndView index(){
    	ModelAndView view = new ModelAndView();  
        view.setViewName("index"); //name of the jsp-file in the "page" folder  
          
        String str = "MVC Spring is here!";  
        view.addObject("message", str); //adding of str object as "message" parameter  
          
        return view;  
    }
    
}
