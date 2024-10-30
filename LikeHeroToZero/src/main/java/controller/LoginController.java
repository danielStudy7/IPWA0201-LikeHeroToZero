package controller;

import java.io.Serializable;
import java.util.List;

import dao.UserDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.User;

@Named
@ViewScoped
public class LoginController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UserDAO userDao;
	
	@Inject
	private UserSessionController userSession;
	
	private User loginUser;
	
	
	public LoginController()
	{
		loginUser = new User("", "");
	}
	
	
	public String login()
	{
		List<User> userList = userDao.getUserList();
		
		this.loginUser.setPassword(Integer.toString(loginUser.getPassword().hashCode()));
		
		for (User user : userList)
		{	
			if (user.equals(this.loginUser))
			{
				userSession.setCurrentUser(userDao.getUser(loginUser.getUserName()));
				return "backend.xhtml";
			}
		}
		
		return "login.xhtml";
	}
	
	public String logout()
	{
		if (userSession.getCurrentUser() != null)
		{	
			userSession = new UserSessionController();
			
			return "index.xhtml";
		}
		else 
		{			
			return "index.xhtml";
		}
	}
	
	public User getLoginUser()
	{
		return loginUser;
	}
	
	public void setLoginUser(User user)
	{
		this.loginUser = user;
	}
}
