package controller;

import java.io.Serializable;

import org.primefaces.event.SelectEvent;

import dao.ChangeEntryDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lazyDataModel.LazyEmissionEntryDataModel;
import model.ChangeEntry;
import model.EmissionEntry;

@Named
@ViewScoped
public class BackendController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private LazyEmissionEntryDataModel lazyDataModel;
	private EmissionEntry emissionEntry;
	private EmissionEntry selectedEmissionEntry;
	private ChangeEntry changeEntry;
	
	@Inject
	private ChangeEntryDAO changeEntryDao;
	
	@Inject
	private UserSessionController userSession;

	
	//Konstruktor
	public BackendController()
	{
		changeEntry = new ChangeEntry();
		lazyDataModel = new LazyEmissionEntryDataModel();
	}
	
	
	//Übernahme des selektierten EmissionEntry
	public void onRowSelect(SelectEvent<EmissionEntry> event)
	{
		selectedEmissionEntry = event.getObject();
	}
	
	//Neuen ChangeEntry erstellen
	//Übernimmt Daten aus dem vorherigen EmissionEntry, wenn diese nicht gefüllt wurden
	public void createChangeEntry()
	{	
		if (selectedEmissionEntry != null)
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
		else
		{
			
		}
	}
	
	
	public void edit()
	{
		//Leere Methode zum Übernehmen der Einträge
	}
	
	
	//Getter Setter
	public LazyEmissionEntryDataModel getLazyDataModel()
	{
		return lazyDataModel;
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
