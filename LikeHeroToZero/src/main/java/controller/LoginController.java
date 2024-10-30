package controller;

import java.io.Serializable;
import java.util.List;

import dao.UserDAO;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
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
		userSession.setCurrentUser(userDao.getUser(loginUser.getUserName()));
		return "backend.xhtml";
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
	
	public void validateLogin(FacesContext context, UIComponent component, Object object) throws ValidatorException
	{
		List<User> userList = userDao.getUserList();
		
		String tempPass = (String) object;
		
		loginUser.setPassword(Integer.toString(tempPass.hashCode()));
		
		for (User user : userList)
		{	
			if (user.equals(loginUser))
			{
				return;
			}
		}
		
		throw new ValidatorException(new FacesMessage("Benutzername oder Passwort falsch!"));
	}
	
	
	public void postValidateUser(ComponentSystemEvent event) throws AbortProcessingException
	{
		UIInput temp = (UIInput) event.getComponent();
		this.loginUser.setUserName((String)temp.getValue());
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
