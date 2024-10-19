package controller;

import java.io.Serializable;

import dao.CountryDAO;
import dao.UserDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Country;

@Named
@ViewScoped
public class NewEntryController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Country country;
	
	
	@Inject
	private CountryDAO countryDao;
	
	@Inject
	private UserDAO userDao;
	
	@Inject
	private UserSessionController userSession;

	
	public NewEntryController()
	{
		country = new Country();
	}

	
	public void createCountry()
	{
		userDao.updateUser(userSession.getCurrentUser());
		country.setUser(userSession.getCurrentUser());
		countryDao.createCountryEntry(country);
		country = new Country();
	}
	
	
	public Country getCountry()
	{
		return country;
	}
	
	public void setCountry(Country country)
	{
		this.country = country;
	}
}
