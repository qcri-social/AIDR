package qa.qcri.aidr.predictdb.dto.helper;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.predictdb.dto.UsersDTO;


public class UsersDTOHelper {

	public static Users toUsers(UsersDTO userDTO) {
		if (userDTO != null) {
			Users user = new Users();
			user.setName(userDTO.getName());
			user.setRole(userDTO.getRole());
			user.setUserID(userDTO.getUserID());

			return user;
		} 
		return null; 

	}

	public static UsersDTO toUsersDTO(Users user) {
		if (user != null) {
			UsersDTO userDTO = new UsersDTO();
			userDTO.setName(user.getName());
			userDTO.setRole(user.getRole());
			userDTO.setUserID(user.getUserID());

			return userDTO;
		} 
		return null; 

	}
	
	public static List<Users> toUsersList(List<UsersDTO> list) {
		if (list != null) {
			List<Users> usersList = new ArrayList<Users>();
			for (UsersDTO u: list) {
				usersList.add(UsersDTOHelper.toUsers(u));
			}
			return usersList;
		}
		return null;
	}
	
	public static List<UsersDTO> toUsersDTOList(List<Users> list) {
		if (list != null) {
			List<UsersDTO> usersDTOList = new ArrayList<UsersDTO>();
			for (Users u: list) {
				usersDTOList.add(UsersDTOHelper.toUsersDTO(u));
			}
			return usersDTOList;
		}
		return null;
	}
}
