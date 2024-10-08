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
	
	private User loginUser;
	
	
	public LoginController()
	{
		loginUser = new User("", "");
	}
	
	//TODO hash verwenden für PAssword
	public String login()
	{
		List<User> userList = userDao.getUserList();
		
		this.loginUser.setPassword(Integer.toString(loginUser.getPassword().hashCode()));
		
		for (User user : userList)
		{	
			if (user.equals(this.loginUser))
			{
				return "index.xhtml";
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
					}
				}				
			}
			else 
			{
				userDao.createUser(loginUser);
			}
		}
		
		else
		{
			//TODO handeln
			System.out.println("ELSE Block");
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
