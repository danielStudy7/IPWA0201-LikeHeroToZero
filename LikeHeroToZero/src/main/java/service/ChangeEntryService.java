package service;

import java.util.List;
import java.util.UUID;

import common.FailedOperationException;
import dao.ChangeEntryDAO;
import dao.EmissionEntryDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.ChangeEntry;
import model.EmissionEntry;
import model.User;

@Named
@ApplicationScoped
public class ChangeEntryService {
	
	private ChangeEntryDAO changeEntryDao;
	private EmissionEntryDAO emissionEntryDao;
	
	public ChangeEntryService() {
		
		this(new ChangeEntryDAO(), new EmissionEntryDAO());
	}
	
	public ChangeEntryService(ChangeEntryDAO changeEntryDao, EmissionEntryDAO emissionEntryDao) {
		
		this.changeEntryDao = changeEntryDao;
		this.emissionEntryDao = emissionEntryDao;
	}
	
	public ChangeEntry createChangeEntry(ChangeEntry changeEntry, String emissionEntryUUID, UUID changeUserUUID) throws FailedOperationException {
		
		EmissionEntry emissionEntry = changeEntryDao.getEntity(UUID.fromString(emissionEntryUUID), EmissionEntry.class);
		User user = changeEntryDao.getEntity(changeUserUUID, User.class);
		
		createChangeEntry(changeEntry, emissionEntry, user);
		
		return changeEntry;
	}
	
	public void createChangeEntry(ChangeEntry changeEntry, EmissionEntry emissionEntry, User changeUser) throws FailedOperationException {
		
		if (changeEntry.getCountry() == null)
		{
			changeEntry.setCountry(emissionEntry.getCountry());
		}
		
		if (changeEntry.getEmissions() == 0.0)
		{
			changeEntry.setEmissions(emissionEntry.getEmissions());
		}
		
		if (changeEntry.getYear() == 0)
		{
			changeEntry.setYear(emissionEntry.getYear());
		}
		
		changeEntry.setAccepted(false);
		changeEntry.setDeclined(false);
		
		changeEntry.setEmissionEntry(emissionEntry);
		
		changeEntry.setChangeUser(changeUser);
		changeEntry.setCreateUser(emissionEntry.getUser());
		
		changeEntryDao.createEntity(changeEntry);
	}
	
	public List<ChangeEntry> listAll() {
		
		return changeEntryDao.getEntityList(ChangeEntry.class);
	}
	
	public ChangeEntry getChangeEntry(String uuid) {
		
		return changeEntryDao.getEntity(UUID.fromString(uuid), ChangeEntry.class);
	}
	
	public void acceptChange(ChangeEntry changeEntry) throws FailedOperationException {
		
		EmissionEntry emissionEntry = changeEntry.getEmissionEntry();
		
		acceptChange(changeEntry, emissionEntry, true);
	}
	
	public void declineChange(ChangeEntry changeEntry) throws FailedOperationException {
		
		EmissionEntry emissionEntry = changeEntry.getEmissionEntry();
		
		acceptChange(changeEntry, emissionEntry, false);
	}
	
	public void acceptChange(ChangeEntry changeEntry, EmissionEntry emissionEntry, boolean isAccepted) throws FailedOperationException {
		
		if (isAccepted) {
			changeEntry.setAccepted(true);
		}
		else {
			changeEntry.setDeclined(true);
		}
		
		changeEntryDao.updateEntity(changeEntry);
		
		//EmissionEntry updaten
		emissionEntry.setCountry(changeEntry.getCountry());
		emissionEntry.setEmissions(changeEntry.getEmissions());
		emissionEntry.setYear(changeEntry.getYear());
		
		emissionEntryDao.updateEntity(emissionEntry);
	}
	
	public List<ChangeEntry> getChangesForUser(User user) {
		
		return changeEntryDao.getChangeListByUser(user);
	}
}
