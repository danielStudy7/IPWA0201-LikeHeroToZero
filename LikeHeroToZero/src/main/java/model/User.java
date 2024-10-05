package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User 
{	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String userName;
	private String password;
	
	
	public User()
	{
		//parameterloser Standardkonstruktor
	}
	
	public User(String userName, String password)
	{
		this.userName = userName;
		this.password = password;
	}

	
	public int getId()
	{
		return id;
	}
	
	public String getUserName() 
	{
		return userName;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
}
