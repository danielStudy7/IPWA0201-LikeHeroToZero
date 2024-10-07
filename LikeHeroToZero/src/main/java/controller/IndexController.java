package controller;

import java.io.Serializable;

import dao.CompanyDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Company;
import model.Country;

@Named
@ApplicationScoped
public class IndexController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Company company;
	private Country country;
	
	@Inject
	CompanyDAO companyDao;
	
	public IndexController()
	{
		company = new Company();
		company.getCompanyList();
		
		country = new Country();
		country.getCountryList();
	}
	
	
	public Company getCompany()
	{
		return company;
	}
	
	public Country getCountry()
	{
		return country;
	}
}
