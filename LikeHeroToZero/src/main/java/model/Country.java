package model;

public class Country extends EmissionEntry 
{
	private String continentName;
	
	
	public Country()
	{
		super();
	}
	
	public Country(String countryName, double emissions, int year, String continentName)
	{
		super(countryName, emissions, year);
		this.continentName = continentName;
	}

	
	public String getContinentName() 
	{
		return continentName;
	}

	public void setContinentName(String continentName) 
	{
		this.continentName = continentName;
	}
}
