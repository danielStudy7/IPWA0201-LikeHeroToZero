package model;

import jakarta.persistence.Column;
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
	
	@Column(unique = true)
	private String userName;
	
	private String password;
	
	
	public User()
	{
		
	}
	
	public User(String userName, String password)
	{
		this.userName = userName;
		this.password = password;
	}

	
	@Override
	public boolean equals(Object loginUser)
	{
		if (loginUser instanceof User)
		{
			User user = (User) loginUser;
			if (user.getUserName().equals(this.userName) && user.getPassword().equals(this.password))
			{
				return true;				
			}
			else
			{
				return false;				
			}
		}
		else
		{
			return false;
		}
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
