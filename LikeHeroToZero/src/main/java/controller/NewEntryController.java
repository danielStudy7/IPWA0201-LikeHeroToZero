package controller;

import java.io.Serializable;

import dao.EmissionEntryDAO;
import dao.UserDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.EmissionEntry;

@Named
@ViewScoped
public class NewEntryController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private EmissionEntry emissionEntry;
	
	
	@Inject
	private EmissionEntryDAO emissionDao;
	
	@Inject
	private UserDAO userDao;
	
	@Inject
	private UserSessionController userSession;

	
	public NewEntryController()
	{
		emissionEntry = new EmissionEntry();
	}

	
	public void createEmissionEntry()
	{
		userDao.updateUser(userSession.getCurrentUser());
		emissionEntry.setUser(userSession.getCurrentUser());
		emissionEntry.setChecked(true);
		emissionDao.createEmissionEntry(emissionEntry);
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
