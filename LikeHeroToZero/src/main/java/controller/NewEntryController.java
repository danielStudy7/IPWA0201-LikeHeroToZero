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

	Company company;
	Country country;
	
	@Inject
	private CompanyDAO companyDao;
	
	@Inject
	private CountryDAO countryDao;
	
	public NewEntryController()
	{
		
	}
	
	
	public void createCompany()
	{
		companyDao.createCompanyEntry(company);
	}
	
	public void createCountry()
	{
		
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
