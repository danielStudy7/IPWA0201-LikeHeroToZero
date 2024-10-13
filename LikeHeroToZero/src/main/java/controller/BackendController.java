package controller;

import java.io.Serializable;
import java.util.List;

import dao.CompanyDAO;
import dao.CountryDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Company;
import model.Country;

@Named
@ViewScoped
public class BackendController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Company> companyList;
	private List<Country> countryList;
	
	@Inject
	private CompanyDAO companyDao;
	
	@Inject
	private CountryDAO countryDao;
	
	
	public BackendController()
	{
		
	}
	
	
	public List<Company> getCompanyList()
	{
		companyList = companyDao.findAll();
		System.out.println("CompanyList: " + companyList.get(0).getCompanyName());
		
		return companyList;
	}
	
	public List<Country> getCountryList()
	{
		countryList = countryDao.findAll();
		
		return countryList;
	}
}
