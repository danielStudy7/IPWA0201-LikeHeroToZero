package controller;

import java.io.Serializable;

import common.FailedOperationException;
import dao.EmissionEntryDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.EmissionEntry;
import service.UserService;

@Named
@ViewScoped
public class NewEntryController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private EmissionEntry emissionEntry;
	
	@Inject
	private EmissionEntryDAO emissionDao;
	
	@Inject
	private UserSessionController userSession;
	
	@Inject
	private UserService userService;

	public NewEntryController()
	{
		emissionEntry = new EmissionEntry();
	}

	// TODO EmissionEntryService emissionEntry in den Service reichen
	public void createEmissionEntry() throws FailedOperationException
	{
		userService.updateUser(userSession.getCurrentUser());
		emissionEntry.setUser(userSession.getCurrentUser());
		emissionEntry.setChecked(true);
		emissionDao.createEntity(emissionEntry);
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
