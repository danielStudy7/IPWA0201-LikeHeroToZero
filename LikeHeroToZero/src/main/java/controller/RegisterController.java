package controller;

import java.io.Serializable;
import java.util.List;

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
	
	private String tempUserName;
	
	
	//Konstruktor
	public RegisterController()
	{
		signUpUser = new User("", "");
	}
	
	
	//Neuen User erstellten
	public String signUp()
	{
		if (signUpUser.getUserName() != null && !signUpUser.getUserName().isEmpty() && signUpUser.getPassword() != null && !signUpUser.getPassword().isEmpty())
		{
			userDao.createUser(signUpUser);
			userSession.setCurrentUser(signUpUser);
			userSession.setLoggedIn(true);
			
			return "backend.xhtml";
		}
		else
		{
			return "login.xhtml";
		}
	}
	
	
	//Registrierung validieren
	public void postValidateUserName(ComponentSystemEvent event) throws ValidatorException
	{
		UIInput tempUserName = (UIInput) event.getComponent();
		this.tempUserName = (String) tempUserName.getValue();
	}
	
	public void validateUserName(FacesContext context, UIInput component, Object object) throws ValidatorException
	{
		List<User> userList = userDao.getUserList();
		
		int count = 0;
		
		if (userList != null && !userList.isEmpty())
		{
			for (User user : userList)
			{
				if (user.getUserName().equals(tempUserName))
				{
					count++;
				}
			}
			if (count == 0)
			{
				return;
			}
			else
			{
				throw new ValidatorException(new FacesMessage("Benutzername ist bereits vergeben. Bitte wählen Sie einen anderen."));
			}
		}
		else
		{
			return;
		}
	}
	
	
	//Getter Setter
	public User getSignUpUser()
	{
		return signUpUser;
	}
	
	public void setSignUpUser(User user)
	{
		this.signUpUser = user;
	}
}
