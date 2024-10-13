package controller;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import model.User;

@Named
@SessionScoped
public class UserSessionController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private User currentUser;
	
	
	public UserSessionController()
	{
		currentUser = new User();
	}
	
	
	public User getCurrentUser()
	{
		return currentUser;
	}
	
	public void setCurrentUser(User currentUser)
	{
		this.currentUser = currentUser;
	}
}
