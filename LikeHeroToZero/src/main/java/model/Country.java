package model;

import java.util.List;

import dao.CountryDAO;
import jakarta.persistence.Entity;

@Entity
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

	
	public List<Country> getCountryList()
	{
		CountryDAO countryDao = new CountryDAO();
		List<Country> countryList = countryDao.findAll();
		
		return countryList;
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
