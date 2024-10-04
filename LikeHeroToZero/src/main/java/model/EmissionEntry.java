package model;

public class EmissionEntry 
{
	private String country;
	private double emissions;
	private int year;
	
	
	public EmissionEntry()
	{
		//Leerer Standardkonstruktor
	}
	
	public EmissionEntry(String country, double emissions, int year)
	{
		this.country = country;
		this.emissions = emissions;
		this.year = year;
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
