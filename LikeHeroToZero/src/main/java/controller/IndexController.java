package controller;

import java.io.Serializable;
import java.util.List;

import dao.CountryDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Country;

@Named
@ApplicationScoped
public class IndexController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Country> countryList;
	
	@Inject
	private CountryDAO countryDao;
	
	
	public IndexController()
	{

	}
	
	
	public List<Country> getCountryList()
	{
		countryList = countryDao.findAll();
		
		return countryList;
	}
}
