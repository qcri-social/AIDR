/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 *
 * @author Koushik
 */
@Local
public interface UserResourceFacade {

	public UsersDTO addUser(UsersDTO user);

	public UsersDTO getUserByName(String userName);

	public UsersDTO getUserByID(Long userID);

	public List<UsersDTO> getAllUsers();

}
