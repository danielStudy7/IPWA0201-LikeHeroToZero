package controller;

import java.io.Serializable;
import java.util.List;

import dao.CompanyDAO;
import dao.CountryDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Company;
import model.Country;

@Named
@ApplicationScoped
public class IndexController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Company> companyList;
	private List<Country> countryList;
	
	@Inject
	private CompanyDAO companyDao;
	
	@Inject
	private CountryDAO countryDao;
	
	
	public IndexController()
	{

	}
	
	
	public List<Company> getCompanyList()
	{
		companyList = companyDao.findAll();
		
		return companyList;
	}
	
	public List<Country> getCountryList()
	{
		countryList = countryDao.findAll();
		
		System.out.println("CountryList aus Controller" + countryList.get(0).getContinentName());
		
		return countryList;
	}
}
