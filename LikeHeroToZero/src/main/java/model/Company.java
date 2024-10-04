package model;

public class Company extends EmissionEntry 
{
	private String companyName;
	
	
	public Company()
	{
		super();
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
