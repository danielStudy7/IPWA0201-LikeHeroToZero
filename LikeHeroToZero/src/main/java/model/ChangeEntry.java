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
	
	private User changeUser;
	private String infoText;
	private String source;
	
	@ManyToOne
	private EmissionEntry emissionEntry;
	
	
	public ChangeEntry()
	{
		
	}
	
	public ChangeEntry(int id, boolean accepted, User changeUser, String infoText, String source,
			EmissionEntry emissionEntry) 
	{
		this();
		this.id = id;
		this.accepted = accepted;
		this.changeUser = changeUser;
		this.infoText = infoText;
		this.source = source;
		this.emissionEntry = emissionEntry;
	}
	

	public int getId() 
	{
		return id;
	}

	public boolean isAccepted() 
	{
		return accepted;
	}

	public User getChangeUser() 
	{
		return changeUser;
	}

	public String getInfoText() {
		return infoText;
	}

	public String getSource() 
	{
		return source;
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
	
	public void setChangeUser(User changeUser) 
	{
		this.changeUser = changeUser;
	}
	
	public void setInfoText(String infoText) 
	{
		this.infoText = infoText;
	}
	
	public void setSource(String source) 
	{
		this.source = source;
	}

	public void setEmissionEntry(EmissionEntry emissionEntry) 
	{
		this.emissionEntry = emissionEntry;
	}
	
	
	
}
