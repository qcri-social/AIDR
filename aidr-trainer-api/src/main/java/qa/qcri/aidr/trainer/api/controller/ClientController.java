package qa.qcri.aidr.trainer.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.trainer.api.entity.Client;
import qa.qcri.aidr.trainer.api.service.ClientService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/client")
@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;

	@RequestMapping("/id/{clientid}")
    public Client getCrisisByID(@PathVariable("clientid") Long clientid){
        return clientService.findClientbyID("clientID", clientid) ;
    }


}
