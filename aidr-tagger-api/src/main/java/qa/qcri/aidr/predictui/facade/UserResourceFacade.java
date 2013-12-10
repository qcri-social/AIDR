/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import javax.ejb.Local;
import qa.qcri.aidr.predictui.entities.Users;

/**
 *
 * @author Imran
 */
@Local
public interface UserResourceFacade {
    
   public Users addUser(Users user);
   
    public Users getUserByName(String userName);
   
}
