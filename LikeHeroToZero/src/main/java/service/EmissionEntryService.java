package service;

import java.util.List;

import common.FailedOperationException;
import dao.EmissionEntryDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.EmissionEntry;
import model.User;

@Named
@ApplicationScoped
public class EmissionEntryService {
	
	private EmissionEntryDAO emissionEntryDao;
	
	public EmissionEntryService() {
		this(new EmissionEntryDAO());
	}
	
	public EmissionEntryService(EmissionEntryDAO emissionEntryDao) {
		this.emissionEntryDao = emissionEntryDao;
	}
	
	public void createEmissionEntry(EmissionEntry emissionEntry, User currentUser) throws FailedOperationException {
		emissionEntry.setUser(currentUser);
		emissionEntry.setChecked(true);
		emissionEntryDao.createEntity(emissionEntry);
	}
	
	public List<EmissionEntry> findAll() {
		return emissionEntryDao.findAll();
	}
}
