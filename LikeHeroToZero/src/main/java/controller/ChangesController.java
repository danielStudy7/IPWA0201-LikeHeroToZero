package controller;

import java.io.Serializable;
import java.util.List;

import org.primefaces.event.SelectEvent;

import common.FailedOperationException;
import dao.ChangeEntryDAO;
import dao.EmissionEntryDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.ChangeEntry;
import model.EmissionEntry;

@Named
@ViewScoped
public class ChangesController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<ChangeEntry> changesList;
	private ChangeEntry changeEntry;
	private ChangeEntry selectedChangeEntry;
	private EmissionEntry emissionEntry;
	
	@Inject
	private UserSessionController userSession;
	
	@Inject
	private ChangeEntryDAO changeEntryDao;
	
	@Inject
	private EmissionEntryDAO emissionEntryDao;
	
	
	//Konstruktor
	public ChangesController()
	{
		
	}
	
	
	// TODO ChangeEntryService
	public void acceptChange() throws FailedOperationException
	{
		if (selectedChangeEntry != null)
		{
			selectedChangeEntry.setAccepted(true);
			changeEntryDao.updateEntity(selectedChangeEntry);
			
			//EmissionEntry updaten
			emissionEntry.setCountry(selectedChangeEntry.getCountry());
			emissionEntry.setEmissions(selectedChangeEntry.getEmissions());
			emissionEntry.setYear(selectedChangeEntry.getYear());
			
			emissionEntryDao.updateEntity(emissionEntry);
			
			selectedChangeEntry = null;
			emissionEntry = null;			
		}
		else
		{
			//Keine weiteren Aktionen notwendig
		}
	}
	
	// TODO ChangeEntryService
	public void declineChange() throws FailedOperationException
	{
		if (selectedChangeEntry != null)
		{
			selectedChangeEntry.setDeclined(true);
			changeEntryDao.updateEntity(selectedChangeEntry);
			
			selectedChangeEntry = null;
			emissionEntry = null;			
		}
		else
		{
			//Keine weiteren Aktionen notwendig
		}
	}
	
	//Übernahme des Tabelleneintrag
	public void onRowSelect(SelectEvent<ChangeEntry> event)
	{
		selectedChangeEntry = event.getObject();
		emissionEntry = event.getObject().getEmissionEntry();
	}
	
	public void edit()
	{
		//Leere Methode zum Übernehmen der Einträge
	}
	
	
	//Getter Setter
	// TODO ChangeEntryService
	public List<ChangeEntry> getChangesList()
	{
		changesList = changeEntryDao.getChangeListByUser(userSession.getCurrentUser());
		
		return changesList;
	}
	
	public ChangeEntry getChangeEntry()
	{
		return changeEntry;
	}
	
	public ChangeEntry getSelectedChangeEntry()
	{
		return selectedChangeEntry;
	}
	
	public EmissionEntry getEmissionEntry()
	{
		return emissionEntry;
	}
	
	public void setChangeEntry(ChangeEntry changeEntry)
	{
		this.changeEntry = changeEntry;
	}
	
	public void setSelectedChangeEntry(ChangeEntry selectedChangeEntry)
	{
		this.selectedChangeEntry = selectedChangeEntry;
	}
}
