package controller;

import java.io.Serializable;

import dao.UserDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.User;
import service.UserService;

@Named
@SessionScoped
public class LoginController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UserDAO userDao;
	
	@Inject
	private UserSessionController userSession;
	
	@Inject
	private UserService userService;
	
	private User loginUser;
	
	public LoginController()
	{
		loginUser = new User("", "");
	}
	
	public String login()
	{
		userSession.setCurrentUser(userDao.getUserByUsername(loginUser.getUserName()));
		userSession.setLoggedIn(true);
		
		return "backend.xhtml";
	}
	
	public String logout()
	{
		if (userSession.getCurrentUser() != null)
		{	
			userSession.setCurrentUser(null);
			userSession.setLoggedIn(false);
			
			loginUser = new User("", "");
			
			return "index.xhtml";
		}
		else 
		{			
			return "index.xhtml";
		}
	}

	public void validateLogin(FacesContext context, UIComponent component, Object object) throws ValidatorException
	{
		String tempPass = (String) object;
		
		if (userService.isLoginValid(loginUser.getUserName(), tempPass)) {
			return;
		}
		
		throw new ValidatorException(new FacesMessage("Benutzername oder Passwort falsch!"));
	}
	
	public void postValidateUser(ComponentSystemEvent event) throws AbortProcessingException
	{
		UIInput temp = (UIInput) event.getComponent();
		this.loginUser.setUserName((String)temp.getValue()); 
	}
	
	public UserSessionController getUserSession()
	{
		return userSession;
	}
	
	public User getLoginUser()
	{
		return loginUser;
	}
	
	public void setUserSession(UserSessionController userSession)
	{
		this.userSession = userSession;
	}
	
	public void setLoginUser(User user)
	{
		this.loginUser = user;
	}
}
