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
public class RegisterController implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UserDAO userDao;
	
	@Inject
	private UserSessionController userSession;
	
	private User signUpUser;
	
	
	public RegisterController()
	{
		signUpUser = new User("", "");
	}
	
	
	public String signUp()
	{
		if (signUpUser.getUserName() != null && signUpUser.getPassword() != null)
		{
			List<User> userList = userDao.getUserList();
			
			int countUser = 0;
			
			if (userList != null && !userList.isEmpty())
			{
				for (User user : userList)
				{
					if (user.getUserName().equals(signUpUser.getUserName()))
					{
						countUser++;
					}
				}
				
				if (countUser == 0)
				{
					userDao.createUser(signUpUser);
					userSession.setCurrentUser(signUpUser);
					
					return "backend.xhtml";
				}
				else
				{
					//User nicht erstellbar
					
					return "register.xhtml";
				}
			}
			else 
			{
				userDao.createUser(signUpUser);
				userSession.setCurrentUser(signUpUser);
				
				return "backend.xhtml";
			}
		}
		
		else
		{
			//TODO handeln
			return "register.xhtml";
		}
	}
	
	
	public User getSignUpUser()
	{
		return signUpUser;
	}
	
	public void setSignUpUser(User user)
	{
		this.signUpUser = user;
	}
}
