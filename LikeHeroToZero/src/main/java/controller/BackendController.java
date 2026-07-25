package controller;

import java.io.Serializable;

import org.primefaces.event.SelectEvent;

import common.FailedOperationException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lazyDataModel.LazyEmissionEntryDataModel;
import model.ChangeEntry;
import model.EmissionEntry;
import service.ChangeEntryService;

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
	private UserSessionController userSession;
	
	@Inject
	private ChangeEntryService changeEntryService;

	public BackendController()
	{
		changeEntry = new ChangeEntry();
		lazyDataModel = new LazyEmissionEntryDataModel();
	}
	
	public void onRowSelect(SelectEvent<EmissionEntry> event)
	{
		selectedEmissionEntry = event.getObject();
	}
	
	public void createChangeEntry() throws FailedOperationException
	{	
		if (selectedEmissionEntry != null)
		{
			changeEntryService.createChangeEntry(changeEntry, selectedEmissionEntry, userSession.getCurrentUser());

			changeEntry = new ChangeEntry();
			selectedEmissionEntry = null;
		}
		else {
			throw new ValidatorException(new FacesMessage("Bitte wählen Sie einen Eintrag zum Ändern aus."));
		}
	}
	
	
	public void edit()
	{
		//Leere Methode zum Übernehmen der Einträge
	}
	
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
