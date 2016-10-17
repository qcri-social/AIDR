/**
 * 
 */
package qa.qcri.aidr.trainer.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.trainer.api.service.DataModelImageExportService;

/**
 * @author Latika
 *
 */
@RestController
public class ImageController {

	@Autowired
	DataModelImageExportService imageExportService;
	
    @RequestMapping("/{collectionCode}/image/count")
    public Long getCrisisByID(@PathVariable("collectionCode") String collectionCode){
        return imageExportService.countImageForCollection(collectionCode);
    }

	
}
