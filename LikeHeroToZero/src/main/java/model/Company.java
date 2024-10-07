package model;

import java.util.List;

import dao.CompanyDAO;
import jakarta.persistence.Entity;

@Entity
public class Company extends EmissionEntry 
{
	private String companyName;
	
	
	public Company()
	{
		super();	
	}
	
	
	public List<Company> getCompanyList()
	{
		CompanyDAO companyDao = new CompanyDAO();
		List<Company> companyList = companyDao.findAll();
		
		System.out.println(companyList.get(0).getCountry() + "Liste Unternehmen");
		
		return companyList;
	}
	
	
	public Company(String countryName, double emissions, int year, String companyName)
	{
		super(countryName, emissions, year);
		this.companyName = companyName;
	}
	
	
	public String getCompanyName() 
	{
		return companyName;
	}

	public void setCompanyName(String companyName) 
	{
		this.companyName = companyName;
	}
}
