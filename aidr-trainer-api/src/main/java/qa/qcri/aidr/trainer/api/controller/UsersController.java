package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.trainer.api.service.UsersService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/20/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/users")
@RestController
public class UsersController {
    protected static Logger logger = Logger.getLogger("UsersController");

    @Autowired
    private UsersService usersService;

    @RequestMapping("/getuser/{username}")
    public Users getUserByName(@PathVariable("username") String username){
        return  usersService.findUserByName(username) ;
    }

}
