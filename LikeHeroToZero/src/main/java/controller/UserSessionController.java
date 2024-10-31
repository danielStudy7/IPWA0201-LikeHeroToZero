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
	
	private boolean loggedIn;
	
	
	public UserSessionController()
	{
		currentUser = new User();
		loggedIn = false;
	}
	
	
	public User getCurrentUser()
	{
		return currentUser;
	}
	
	public boolean isLoggedIn()
	{
		return loggedIn;
	}
	
	public void setCurrentUser(User currentUser)
	{
		this.currentUser = currentUser;
	}
	
	public void setLoggedIn(boolean loggedIn)
	{
		this.loggedIn = loggedIn;
	}
}
