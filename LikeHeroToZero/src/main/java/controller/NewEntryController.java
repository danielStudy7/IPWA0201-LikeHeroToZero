package controller;

import java.io.Serializable;

import common.FailedOperationException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.EmissionEntry;
import service.EmissionEntryService;
import service.UserService;

@Named
@ViewScoped
public class NewEntryController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private EmissionEntry emissionEntry;
	
	@Inject
	private EmissionEntryService emissionEntryService;
	
	@Inject
	private UserSessionController userSession;
	
	@Inject
	private UserService userService;

	public NewEntryController()
	{
		emissionEntry = new EmissionEntry();
	}

	public void createEmissionEntry() throws FailedOperationException
	{
		userService.updateUser(userSession.getCurrentUser());
		emissionEntryService.createEmissionEntry(emissionEntry, userSession.getCurrentUser());
		emissionEntry = new EmissionEntry();
	}

	public EmissionEntry getEmissionEntry()
	{
		return emissionEntry;
	}
	
	public void setEmissionEntry(EmissionEntry emissionEntry)
	{
		this.emissionEntry = emissionEntry;
	}
}
