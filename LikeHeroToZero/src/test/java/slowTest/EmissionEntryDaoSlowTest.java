package slowTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.primefaces.model.SortOrder;

import dao.EmissionEntryDAO;
import jakarta.persistence.NoResultException;
import model.EmissionEntry;
import model.User;

class EmissionEntryDaoSlowTest extends AbstractSlowTestVorlage {

	private EmissionEntryDAO daoUnderTest;
	private User user;
	private EmissionEntry germanyEntry;
	private EmissionEntry spainEntry;
	
	@Override
	void createTestData() {
		daoUnderTest = new EmissionEntryDAO(getEntityManager());
		
		user = new User("admin", "save");
		germanyEntry = new EmissionEntry("Germany", 22.0, 2025, false, user);
		spainEntry = new EmissionEntry("Spain", 32.0, 2025, false, user);
		
		getEntityManager().persist(user);
		getEntityManager().persist(germanyEntry);
		getEntityManager().persist(spainEntry);
		
		getTransaction().commit();
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<EmissionEntry> result = daoUnderTest.findAll();
		
		assertEquals(2, result.size());
	}
	
	@Test
	void testCreateEmissionEntry() throws Exception {
		EmissionEntry newEntry = new EmissionEntry("Portugal", 42.0, 2025, true, user);
		
		daoUnderTest.createEmissionEntry(newEntry);
		EmissionEntry result = daoUnderTest.getEmissionEntry(String.valueOf(newEntry.getId()));
		
		assertEquals(newEntry, result);
	}
	
	@Test
	void testDeleteEmissionEntry() throws Exception {
		daoUnderTest.deleteEmissionEntry(spainEntry);
		
		try {
			daoUnderTest.getEmissionEntry(String.valueOf(spainEntry.getId()));
			fail("Hier sollte eine Exception fliegen, weil der User nicht mehr existiert.");
		}
		catch (NoResultException e) {
			assertEquals("No result found for query [<criteria>]", e.getMessage());	
		}
	}
	
	@Test
	void testUpdateEmissionEntry() throws Exception {
		spainEntry.setEmissions(42.22);
		spainEntry.setChecked(true);
		
		daoUnderTest.updateEmissionEntry(spainEntry);
		EmissionEntry result = daoUnderTest.getEmissionEntry(String.valueOf(spainEntry.getId()));
		
		assertAll(
			() -> assertEquals(spainEntry.getCountry(), result.getCountry()),
			() -> assertEquals(spainEntry.getEmissions(), result.getEmissions()),
			() -> assertEquals(spainEntry.getId(), result.getId()),
			() -> assertEquals(spainEntry.getUser(), result.getUser()),
			() -> assertEquals(spainEntry.getYear(), result.getYear())
		);
	}
	
	@Test
	void testCountEmissionEntrys_WithFilter() throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("country", "germany");
		
		int result = daoUnderTest.countEmissionEntrys(filter);
		
		assertEquals(1, result);
	}
	
	@Test
	void testCountEmissionEntrys_WithoutFilter() throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		
		int result = daoUnderTest.countEmissionEntrys(filter);
		
		assertEquals(2, result);
	}
	
	@Test
	void testLoad_WithoutFilter() throws Exception {
		getTransaction().begin();
		EmissionEntry portugalEntry = new EmissionEntry("Portugal", 42.0, 2026, true, user);
		getEntityManager().persist(portugalEntry);
		
		germanyEntry.setChecked(true);
		getEntityManager().merge(germanyEntry);
		spainEntry.setChecked(true);
		getEntityManager().merge(spainEntry);
		getTransaction().commit();
		
		int first = 0;
		int pageSize = 3;
		String sortField = "year";
		
		List<EmissionEntry> result = daoUnderTest.loadEmissionEntrys(first, pageSize, sortField, SortOrder.ASCENDING, null);
		
		assertEquals(3, result.size());
		assertEquals(germanyEntry, result.get(0));
		assertEquals(spainEntry, result.get(1));
		assertEquals(portugalEntry, result.get(2));
	}
	
	@Test
	void testLoad_Pagination() throws Exception {
		getTransaction().begin();
		germanyEntry.setChecked(true);
		getEntityManager().merge(germanyEntry);
		spainEntry.setChecked(true);
		getEntityManager().merge(spainEntry);
		getTransaction().commit();
		
		int first = 0;
		int pageSize = 1;
		String sortField = "year";
		
		// Page 1
		List<EmissionEntry> result = daoUnderTest.loadEmissionEntrys(first, pageSize, sortField, SortOrder.ASCENDING, null);
		
		assertEquals(1, result.size());
		assertEquals(germanyEntry, result.get(0));
		
		// Page 2
		result = daoUnderTest.loadEmissionEntrys(1, pageSize, sortField, SortOrder.ASCENDING, null);
		
		assertEquals(1, result.size());
		assertEquals(spainEntry, result.get(0));
	}
	
	@Test
	void testLoad() throws Exception {
		getTransaction().begin();
		EmissionEntry portugalEntry = new EmissionEntry("Portugal", 42.0, 2026, true, user);
		getEntityManager().persist(portugalEntry);
		
		germanyEntry.setChecked(true);
		getEntityManager().merge(germanyEntry);
		spainEntry.setChecked(true);
		getEntityManager().merge(spainEntry);
		getTransaction().commit();
		
		int first = 0;
		int pageSize = 2;
		String sortField = "emissions";

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("year", "2025");
		
		List<EmissionEntry> result = daoUnderTest.loadEmissionEntrys(first, pageSize, sortField, SortOrder.DESCENDING, filter);
		
		assertEquals(2, result.size());
		assertEquals(spainEntry, result.get(0));
		assertEquals(germanyEntry, result.get(1));
	}
}
