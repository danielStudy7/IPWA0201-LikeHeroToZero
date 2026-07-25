package controller;

import java.io.Serializable;
import java.util.List;

import org.primefaces.event.SelectEvent;

import common.FailedOperationException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.ChangeEntry;
import model.EmissionEntry;
import service.ChangeEntryService;

@Named
@ViewScoped
public class ChangesController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ChangeEntry changeEntry;
	private ChangeEntry selectedChangeEntry;
	private EmissionEntry emissionEntry;
	
	@Inject
	private UserSessionController userSession;
	
	@Inject
	private ChangeEntryService changeEntryService;
	
	public ChangesController()
	{
		// JSF
	}
	
	public void acceptChange() throws FailedOperationException, ValidatorException
	{
		if (selectedChangeEntry != null)
		{
			changeEntryService.acceptChange(selectedChangeEntry, emissionEntry, true);
			
			selectedChangeEntry = null;
			emissionEntry = null;			
		} 
		else {
			throw new ValidatorException(new FacesMessage("Wählen Sie einen Eintrag zum Akzeptieren aus."));
		}
	}
	
	public void declineChange() throws FailedOperationException
	{
		if (selectedChangeEntry != null)
		{
			changeEntryService.acceptChange(selectedChangeEntry, emissionEntry, false);
			
			selectedChangeEntry = null;
			emissionEntry = null;			
		}
		else {
			throw new ValidatorException(new FacesMessage("Wählen Sie einen Eintrag zum Ablehnen aus."));
		}
	}
	
	public void onRowSelect(SelectEvent<ChangeEntry> event)
	{
		ChangeEntry object = event.getObject();
		selectedChangeEntry = object;
		emissionEntry = object.getEmissionEntry();
	}
	
	public void edit()
	{
		//Leere Methode zum Übernehmen der Einträge
	}
	
	public List<ChangeEntry> getChangesList()
	{
		return changeEntryService.getChangesForUser(userSession.getCurrentUser());
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
