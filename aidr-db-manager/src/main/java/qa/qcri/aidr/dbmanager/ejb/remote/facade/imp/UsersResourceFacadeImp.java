package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;
import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.dbmanager.entities.task.Document;


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
	public UsersDTO getUserByName(String name) throws PropertyNotSetException {
		List<Users> usersList = (List<Users>) getAllByCriteria(Restrictions.eq("name", name));
		if(usersList != null && !usersList.isEmpty()){
			return new UsersDTO(usersList.get(0));
		}
		return null;
	}

	@Override
	public UsersDTO getUserById(Long id) throws PropertyNotSetException {
		Users user = getById(id);
		if (user != null) {
			return new UsersDTO(user);
		}
		return null;
	}

	@Override
	public List<UsersDTO> getAllUsersByName(String name) throws PropertyNotSetException {
		List<UsersDTO> dtoList = new ArrayList<UsersDTO>();
		List<Users> usersList = (List<Users>) getAllByCriteria(Restrictions.eq("name", name));
		if (usersList != null && !usersList.isEmpty()){
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

	@Override
	public UsersDTO addUser(UsersDTO user) {
		try {
			Users u = user.toEntity();
			em.persist(u);
			em.flush();
			em.refresh(u);
			return new UsersDTO(u);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Integer deleteUser(Long id) {
		Users user = getById(id);
		if (user != null) {
			delete(user);
			em.flush();
			return 1;
		}
		else {
			throw new RuntimeException("User requested to be deleted does not exist! id = " + id);
		}
	}

	@Override
	public List<UsersDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException {
		List<Users> list = getAllByCriteria(Restrictions.eq(columnName,value));
		List<UsersDTO> dtoList = new ArrayList<UsersDTO>();
		if (list != null && !list.isEmpty()) {
			for (Users c: list) {
				dtoList.add(new UsersDTO(c));
			}
		}
		return dtoList;
	}

	@Override
	public List<UsersDTO> getAllUsers() throws PropertyNotSetException {
		List<UsersDTO> dtoList = new ArrayList<UsersDTO>();
		List<Users> list = this.getAll();
		if (list != null && !list.isEmpty()) {
			for (Users u: list) {
				dtoList.add(new UsersDTO(u));
			}
		}
		return dtoList;
	}
}
