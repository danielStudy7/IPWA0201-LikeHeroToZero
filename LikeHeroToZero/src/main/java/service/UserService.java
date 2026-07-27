package service;

import java.util.List;

import common.FailedOperationException;
import dao.UserDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.NoResultException;
import model.User;

@Named
@ApplicationScoped
public class UserService {
	
	private UserDAO userDao;
	
	public UserService() {
		this.userDao = new UserDAO();
	}
	
	
	public boolean isLoginValid(String username, String passwort) {
		boolean result = false;
		
		List<User> userList = userDao.getEntityList(User.class);
		
		for (User user : userList) {
			if (user.getUserName().equals(username)) {
				result = user.getPassword().equals(passwort);
			}
		}
		
		return result;
	}
	
	public boolean isUsernameValid(String username) {
		boolean result = false;
		
		List<User> userList = userDao.getEntityList(User.class);
		
		if (userList.isEmpty()) {
			return true;
		}
		
		for (User user : userList) {
			result = user.getUserName().equals(username) ? false : true;
		}
		
		return result;
	}
	
	public void createUser(User newUser) throws FailedOperationException {
		newUser.setPassword(Integer.toString(newUser.getPassword().hashCode()));
		userDao.createEntity(newUser);
	}
	
	public void updateUser(User user) throws FailedOperationException {
		userDao.updateEntity(user);
	}
	
	public User getUserByUsername(String username) {
		User result = null;
		
		try {
			result = userDao.getUserByUsername(username);
		} 
		catch (NoResultException e) {
			// Keine Exception werfen, null zurück
		}

		return result;
	}
}
