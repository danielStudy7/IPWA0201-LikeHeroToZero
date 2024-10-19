package controller;

import java.io.Serializable;
import java.util.List;

import dao.CountryDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Country;

@Named
@ViewScoped
public class BackendController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private List<Country> countryList;
	
	@Inject
	private CountryDAO countryDao;
	
	
	public BackendController()
	{
		
	}
	
	
	public List<Country> getCountryList()
	{
		countryList = countryDao.findAll();
		
		return countryList;
	}
}
