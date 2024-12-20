package model;

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
	
	private boolean checked;
	
	@ManyToOne
	private User user;

	
	//Konstruktoren
	public EmissionEntry()
	{
		
	}
	
	public EmissionEntry(String country, double emissions, int year, boolean checked, User user)
	{
		this.country = country;
		this.emissions = emissions;
		this.year = year;
		this.checked = checked;
		this.user = user;
	}
	
	
	//Überschriebene Methoden
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof EmissionEntry)
		{
			EmissionEntry entry = (EmissionEntry) obj;
			
			return this.id == entry.id;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		String tempId = Integer.toString(id);
		
		return tempId.hashCode() * 7;
	}

	
	//Getter Setter
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
