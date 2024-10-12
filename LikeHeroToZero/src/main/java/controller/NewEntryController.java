package controller;

import java.io.Serializable;

import dao.CompanyDAO;
import dao.CountryDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Company;
import model.Country;

@Named
@ViewScoped
public class NewEntryController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Company company;
	private Country country;
	
	@Inject
	private CompanyDAO companyDao;
	
	@Inject
	private CountryDAO countryDao;
	
	public NewEntryController()
	{
		company = new Company();
		country = new Country();
	}
	
	
	public void createCompany()
	{
		companyDao.createCompanyEntry(company);
		company = new Company();
	}
	
	public void createCountry()
	{
		countryDao.createCountryEntry(country);
		country = new Country();
	}
	
	
	public Company getCompany()
	{
		return company;
	}
	
	public Country getCountry()
	{
		return country;
	}
	
	public void setCompany(Company company)
	{
		this.company = company;
	}
	
	public void setCountry(Country country)
	{
		this.country = country;
	}
}
