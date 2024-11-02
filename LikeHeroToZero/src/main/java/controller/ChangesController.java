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
	
	
	//Methoden zum Akzeptieren und Ablehnen
	public void acceptChange()
	{
		if (selectedChangeEntry != null)
		{
			changeEntryDao.acceptChangeEntry(selectedChangeEntry);
			
			//EmissionEntry updaten
			emissionEntry.setCountry(selectedChangeEntry.getCountry());
			emissionEntry.setEmissions(selectedChangeEntry.getEmissions());
			emissionEntry.setYear(selectedChangeEntry.getYear());
			
			emissionEntryDao.updateEmissionEntry(emissionEntry);
			
			selectedChangeEntry = null;
			emissionEntry = null;			
		}
		else
		{
			//Keine weiteren Aktionen notwendig
		}
	}
	
	public void declineChange()
	{
		if (selectedChangeEntry != null)
		{
			changeEntryDao.declineChangeEntry(selectedChangeEntry);
			
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
	public List<ChangeEntry> getChangesList()
	{
		changesList = changeEntryDao.getChangeList(userSession.getCurrentUser());
		
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
