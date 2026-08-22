package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.primefaces.model.SortOrder;

import common.FailedOperationException;
import dao.EmissionEntryDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.Country;
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
	
	public List<EmissionEntry> listEmissionEntrysPaginiert(int page, int entriesPerPage, String sortField, boolean sortDescending, Country filterCountry, Integer filterYear) {
		int first = page * entriesPerPage;
		
		SortOrder sortOrder = sortDescending ? SortOrder.DESCENDING : SortOrder.ASCENDING;
		
		Map<String, Object> filter = createFilterMap(filterCountry, filterYear);
		
		return emissionEntryDao.loadEmissionEntrys(first, entriesPerPage, sortField, sortOrder, filter);
	}
	
	public int countEmissionEntrys(Country filterCountry, Integer filterYear) {
		
		return emissionEntryDao.countEmissionEntrys(createFilterMap(filterCountry, filterYear));
	}

	private Map<String, Object> createFilterMap(Country filterCountry, Integer filterYear) {
		Map<String, Object> filter = new HashMap<String, Object>();
		
		if (filterCountry != null) {
			filter.put("country", filterCountry);
		}
		
		if (filterYear != null) {
			filter.put("year", filterYear);
		}
		return filter;
	}
}
