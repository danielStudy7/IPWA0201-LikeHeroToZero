package controller;

import java.io.Serializable;
import java.util.List;

import org.primefaces.event.SelectEvent;

import dao.ChangeEntryDAO;
import dao.EmissionEntryDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.ChangeEntry;
import model.EmissionEntry;

@Named
@ViewScoped
public class BackendController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private List<EmissionEntry> emissionList;
	private EmissionEntry emissionEntry;
	private EmissionEntry selectedEmissionEntry;
	private ChangeEntry changeEntry;
	
	@Inject
	private EmissionEntryDAO emissionDao;
	
	@Inject
	private ChangeEntryDAO changeEntryDao;
	
	@Inject
	private UserSessionController userSession;

	
	public BackendController()
	{
		changeEntry = new ChangeEntry();
	}
	
	
	public void onRowSelect(SelectEvent<EmissionEntry> event)
	{
		selectedEmissionEntry = event.getObject();
	}
	
	public void createChangeEntry()
	{		
		if (changeEntry.getCountry() == null || changeEntry.getCountry() == "")
		{
			changeEntry.setCountry(selectedEmissionEntry.getCountry());
		}
			
		if (changeEntry.getEmissions() == 0.0)
		{
			changeEntry.setEmissions(selectedEmissionEntry.getEmissions());
		}
				
		if (changeEntry.getYear() == 0)
		{
			changeEntry.setYear(selectedEmissionEntry.getYear());
		}
		
		changeEntry.setAccepted(false);
		changeEntry.setDeclined(false);
		
		changeEntry.setEmissionEntry(selectedEmissionEntry);
		
		changeEntry.setChangeUser(userSession.getCurrentUser());
		changeEntry.setCreateUser(selectedEmissionEntry.getUser());

		changeEntryDao.createChangeEntry(changeEntry);
		
		changeEntry = new ChangeEntry();
		
		selectedEmissionEntry = null;
	}
	
	
	public void edit()
	{
		//nix
	}
	
	
	public List<EmissionEntry> getEmissionList()
	{
		emissionList = emissionDao.findAll();
		
		return emissionList;
	}
	
	public EmissionEntry getEmissionEntry()
	{
		return emissionEntry;
	}
	
	public EmissionEntry getSelectedEmissionEntry()
	{
		return selectedEmissionEntry;
	}
	
	public ChangeEntry getChangeEntry()
	{
		return changeEntry;
	}
	
	
	public void setSelectedEmissionEntry(EmissionEntry selectedEmissionEntry)
	{
		this.selectedEmissionEntry = selectedEmissionEntry;
	}
}
