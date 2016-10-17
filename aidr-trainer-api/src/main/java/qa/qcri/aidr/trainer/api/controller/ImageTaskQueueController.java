package qa.qcri.aidr.trainer.api.controller;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.trainer.api.entity.ImageTaskQueue;
import qa.qcri.aidr.trainer.api.service.ImageTaskQueueService;

@RequestMapping("/taggedImage")
@RestController
public class ImageTaskQueueController {
	
    @Autowired
    private ImageTaskQueueService imageTaskQueueService;

    private static Logger logger = Logger.getLogger(ImageTaskQueueController.class);

    @RequestMapping("/getCount/{crisisID}")
    public Long getImageTaskCount(@PathVariable("crisisID") Long crisisID){
        return imageTaskQueueService.getCountImageTaskByCrisis(crisisID);
    }

    @RequestMapping("/get")
    public List<ImageTaskQueue> getImageTasks(@QueryParam("crisisID") long crisisID,
			@DefaultValue("0") @QueryParam("fromRecord") int fromRecord,
			@DefaultValue("100") @QueryParam("limit") int limit,
			@DefaultValue("") @QueryParam("sortColumn") String sortColumn,
			@DefaultValue("") @QueryParam("sortDirection") String sortDirection){
        return imageTaskQueueService.getImageTaskQueueByCrisis(crisisID);

    }
}
