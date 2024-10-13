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
		//userSession = new UserSessionController();
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
				System.out.println("BenutzerSession LoginController " + userSession.getCurrentUser().getUserName());
				return "newEntry.xhtml";
			}
		}
		
		return "login.xhtml";
	}
	
	public void signUp()
	{
		if (loginUser.getUserName() != null && loginUser.getPassword() != null)
		{
			List<User> userList = userDao.getUserList();
			if (userList != null && !userList.isEmpty())
			{
				for (User user : userList)
				{
					if (user.getUserName().equals(loginUser.getUserName()))
					{
						//User nicht erstellbar
					}
					else
					{
						userDao.createUser(loginUser);
						login();
						userSession.setCurrentUser(loginUser);
					}
				}				
			}
			else 
			{
				userDao.createUser(loginUser);
				login();
				userSession.setCurrentUser(loginUser);
			}
		}
		
		else
		{
			//TODO handeln
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
