package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class EmissionEntry 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String country;
	private double emissions;
	private int year;
	
	
	public EmissionEntry()
	{
		//parameterloser Standardkonstruktor
	}
	
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
}
