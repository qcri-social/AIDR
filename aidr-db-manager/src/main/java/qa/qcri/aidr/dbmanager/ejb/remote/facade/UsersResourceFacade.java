package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Users;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface UsersResourceFacade extends CoreDBServiceFacade<Users, Long> {
	public UsersDTO getUserByName(String name) throws PropertyNotSetException;
	public UsersDTO getUserById(Long id) throws PropertyNotSetException;
	
	public List<UsersDTO> getAllUsersByName(String name) throws PropertyNotSetException;
	public List<CrisisDTO> findAllCrisisByUserID(Long id) throws PropertyNotSetException;
}
