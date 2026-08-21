package service;

import java.util.List;
import java.util.UUID;

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
	
	public EmissionEntry createAndReturnEmissionEntry(EmissionEntry emissionEntry, UUID currentUserId) throws FailedOperationException {
		
		emissionEntry.setUser(emissionEntryDao.getEntity(currentUserId, User.class));
		emissionEntry.setChecked(true);
		emissionEntryDao.createEntity(emissionEntry);
		
		return emissionEntry;
	}
	
	public List<EmissionEntry> findAll() {
		return emissionEntryDao.findAll();
	}
}
