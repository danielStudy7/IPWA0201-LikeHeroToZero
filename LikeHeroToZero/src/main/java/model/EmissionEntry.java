package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class EmissionEntry 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String country;
	private double emissions;
	private int year;
	
	@Column(columnDefinition = "BOOLEAN")
	private boolean checked;
	
	@ManyToOne
	private User user;

	
	public EmissionEntry()
	{
		
	}
	//TODO Konstruktor erweitern
	public EmissionEntry(String country, double emissions, int year)
	{
		this.country = country;
		this.emissions = emissions;
		this.year = year;
	}

	
	public int getId()
	{
		return id;
	}
	
	public String getCountry() 
	{
		return country;
	}

	public double getEmissions() 
	{
		return emissions;
	}

	public int getYear() 
	{
		return year;
	}
	
	public boolean isChecked()
	{
		return checked;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setCountry(String country) 
	{
		this.country = country;
	}

	public void setEmissions(double emissions) 
	{
		this.emissions = emissions;
	}
	
	public void setYear(int year) 
	{
		this.year = year;
	}
	
	public void setChecked(boolean check)
	{
		this.checked = check;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
}
