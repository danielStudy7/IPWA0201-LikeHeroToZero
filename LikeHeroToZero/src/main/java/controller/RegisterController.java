package controller;

import java.io.Serializable;

import common.FailedOperationException;
import dao.UserDAO;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.User;
import service.UserService;

@Named
@ViewScoped
public class RegisterController implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UserSessionController userSession;
	
	@Inject
	private UserService userService;
	
	private User signUpUser;
	
	private String tempUserName;
	
	public RegisterController()
	{
		signUpUser = new User("", "");
	}
	
	public String signUp() throws FailedOperationException
	{
		if (signUpUser.getUserName() != null && !signUpUser.getUserName().isEmpty() && signUpUser.getPassword() != null && !signUpUser.getPassword().isEmpty())
		{
			userService.createUser(signUpUser);
			userSession.setCurrentUser(signUpUser);
			userSession.setLoggedIn(true);
			
			return "backend.xhtml";
		}
		else
		{
			return "login.xhtml";
		}
	}
	
	public void postValidateUserName(ComponentSystemEvent event) throws ValidatorException
	{
		UIInput tempUserName = (UIInput) event.getComponent();
		this.tempUserName = (String) tempUserName.getValue();
	}
	
	public void validateUserName(FacesContext context, UIInput component, Object object) throws ValidatorException
	{
		if (userService.isUsernameValid(tempUserName)) {
			return;
		}
		
		throw new ValidatorException(new FacesMessage("Benutzername ist bereits vergeben. Bitte wählen Sie einen anderen."));
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
