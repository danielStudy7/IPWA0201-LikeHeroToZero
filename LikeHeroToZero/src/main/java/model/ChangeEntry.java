package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ChangeEntry 
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private boolean accepted;
	private boolean declined;
	private double emissions;
	private int year;
	
	@ManyToOne
	private User changeUser;
	
	@ManyToOne
	private User createUser;
	
	private String infoText;
	private String source;
	private String country;
	
	@ManyToOne
	private EmissionEntry emissionEntry;
	
	
	public ChangeEntry()
	{
		
	}
	
	public ChangeEntry(boolean accepted, boolean declined, double emissions, int year, User changeUser, String infoText, String source,
			String country, EmissionEntry emissionEntry) 
	{
		this();
		this.accepted = accepted;
		this.declined = declined;
		this.emissions = emissions;
		this.year = year;
		this.changeUser = changeUser;
		this.infoText = infoText;
		this.source = source;
		this.country = country;
		this.emissionEntry = emissionEntry;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ChangeEntry)
		{
			ChangeEntry changeEntry = (ChangeEntry) obj;
			
			return this.id == changeEntry.id;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		String tempId = Integer.toString(id);
		
		return tempId.hashCode() * 7;
	}
	

	public int getId() 
	{
		return id;
	}

	public boolean isAccepted() 
	{
		return accepted;
	}
	
	public boolean isDeclined()
	{
		return declined;
	}
	
	public double getEmissions()
	{
		return emissions;
	}
	
	public int getYear()
	{
		return year;
	}

	public User getChangeUser() 
	{
		return changeUser;
	}
	
	public User getCreateUser()
	{
		return createUser;
	}

	public String getInfoText() {
		return infoText;
	}

	public String getSource() 
	{
		return source;
	}
	
	public String getCountry()
	{
		return country;
	}

	public EmissionEntry getEmissionEntry() 
	{
		return emissionEntry;
	}
	
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public void setAccepted(boolean accepted) 
	{
		this.accepted = accepted;
	}
	
	public void setDeclined(boolean declined)
	{
		this.declined = declined;
	}
	
	public void setEmissions(double emissions)
	{
		this.emissions = emissions;
	}
	
	public void setYear(int year)
	{
		this.year = year;
	}
	
	public void setChangeUser(User changeUser) 
	{
		this.changeUser = changeUser;
	}
	
	public void setCreateUser(User createUser)
	{
		this.createUser = createUser;
	}
	
	public void setInfoText(String infoText) 
	{
		this.infoText = infoText;
	}
	
	public void setSource(String source) 
	{
		this.source = source;
	}
	
	public void setCountry(String country)
	{
		this.country = country;
	}

	public void setEmissionEntry(EmissionEntry emissionEntry) 
	{
		this.emissionEntry = emissionEntry;
	}
	
}
