package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;

import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.misc.Users;


/**
 * @author Koushik
 */

@Stateless(name="UsersResourceFacadeImp")
public class UsersResourceFacadeImp extends CoreDBServiceFacadeImp<Users, Long> implements UsersResourceFacade {

	private Logger logger = Logger.getLogger("db-manager-log");

	protected UsersResourceFacadeImp(){
		super(Users.class);
	}

	@Override
	public UsersDTO findUserByName(String name) throws PropertyNotSetException {
		List<Users> usersList = (List<Users>) getAllByCriteria(Restrictions.eq("name", name));
		if(usersList != null){
			return new UsersDTO(usersList.get(0));
		}
		return null;
	}

	@Override
	public UsersDTO findUserByID(Long id) throws PropertyNotSetException {
		Users user = getById(id);
		if (user != null) {
			return new UsersDTO(user);
		}
		return null;
	}

	@Override
	public List<UsersDTO> findAllUsersByName(String name) throws PropertyNotSetException {
		List<UsersDTO> dtoList = new ArrayList<UsersDTO>();
		List<Users> usersList = (List<Users>) getAllByCriteria(Restrictions.eq("name", name));
		if (usersList != null){
			for (Users u: usersList) {
				dtoList.add(new UsersDTO(u));
			}
		}
		return dtoList;
	}

	@Override
	public List<CrisisDTO> findAllCrisisByUserID(Long id) throws PropertyNotSetException {
		Users u = getById(id);
		List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
		if (u != null) {
			Hibernate.initialize(u.getCrisises());
			for (Crisis c: u.getCrisises()) {
				Hibernate.initialize(c.getModelFamilies());		// fetching lazily loaded data
				dtoList.add(new CrisisDTO(c));
			}
		}
		return dtoList;
	}
}
