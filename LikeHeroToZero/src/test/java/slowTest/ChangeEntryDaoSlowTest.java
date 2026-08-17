package slowTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import dao.ChangeEntryDAO;
import jakarta.persistence.NoResultException;
import model.ChangeEntry;
import model.Country;
import model.EmissionEntry;
import model.User;

public class ChangeEntryDaoSlowTest extends AbstractSlowTestVorlage {

	private ChangeEntryDAO daoUnderTest;
	private User changeUser;
	private User createUser; 
	private EmissionEntry sourceEmissionEntry;
	private ChangeEntry changeEntry;
	
	@Override
	public void createTestData() {
		daoUnderTest = new ChangeEntryDAO(getEntityManager());
		
		changeUser = new User("changer", "changePasswortPlease");
		createUser = new User("creater", "createNewPasswort");
		sourceEmissionEntry = new EmissionEntry(Country.GERMANY, 19.99, 2025, true, createUser);
		changeEntry = new ChangeEntry(false, false, 555.45, 2025, changeUser, createUser, "delete", "source: dele.te", Country.GERMANY, sourceEmissionEntry);
		
		getEntityManager().persist(changeUser);
		getEntityManager().persist(createUser);
		getEntityManager().persist(sourceEmissionEntry);
		getEntityManager().persist(changeEntry);
		
		getTransaction().commit();
	}
	
	@Test
	void testCreateChangeEntry() throws Exception {
		ChangeEntry newChangeEntry = new ChangeEntry(false, false, 23.45, 2025, changeUser, createUser, "InfoText", "source: bild.de", Country.GERMANY, sourceEmissionEntry);
		
		daoUnderTest.createEntity(newChangeEntry);
		ChangeEntry result = daoUnderTest.getEntity(newChangeEntry.getId(), ChangeEntry.class);
		
		assertNotNull(result);
		assertAll(
			() -> assertEquals(changeUser, result.getChangeUser()),
			() -> assertEquals(createUser, result.getCreateUser()),
			() -> assertEquals(newChangeEntry.getCountry(), result.getCountry()),
			() -> assertEquals(sourceEmissionEntry, result.getEmissionEntry()),
			() -> assertEquals(newChangeEntry.getEmissions(), result.getEmissions()),
			() -> assertEquals(newChangeEntry.getId(), result.getId()),
			() -> assertEquals(newChangeEntry.getInfoText(), result.getInfoText()),
			() -> assertEquals(newChangeEntry.getYear(), result.getYear()),
			() -> assertEquals(newChangeEntry.getSource(), result.getSource())
		);
	}
	
	@Test
	void testDeleteChangeEntry() throws Exception {
		daoUnderTest.deleteEntity(changeEntry);
		
		try {
			daoUnderTest.getEntity(changeEntry.getId(), ChangeEntry.class);
			fail("Hier sollte eine Exception fliegen, weil der User nicht mehr existiert.");
		}
		catch ( NoResultException e) {
			assertEquals("No result found for query [<criteria>]", e.getMessage());
		}
	}
	
	@Test
	void testUpdateChangeEntry() throws Exception {
		changeEntry.setAccepted(true);
		changeEntry.setDeclined(false);
		changeEntry.setEmissions(12.0);
		changeEntry.setSource("who.com");
		changeEntry.setInfoText("Bleibt in diesem Fall");
		
		daoUnderTest.updateEntity(changeEntry);
		ChangeEntry result = daoUnderTest.getEntity(changeEntry.getId(), ChangeEntry.class);
	
		assertNotNull(result);
		assertAll(
			() -> assertEquals(changeUser, result.getChangeUser()),
			() -> assertEquals(createUser, result.getCreateUser()),
			() -> assertEquals(changeEntry.getCountry(), result.getCountry()),
			() -> assertEquals(sourceEmissionEntry, result.getEmissionEntry()),
			() -> assertEquals(changeEntry.getEmissions(), result.getEmissions()),
			() -> assertEquals(changeEntry.getId(), result.getId()),
			() -> assertEquals(changeEntry.getInfoText(), result.getInfoText()),
			() -> assertEquals(changeEntry.getYear(), result.getYear()),
			() -> assertEquals(changeEntry.getSource(), result.getSource())
		);
	}
	
	@Test
	void testAcceptChangeEntry() throws Exception {
		changeEntry.setAccepted(true);
		daoUnderTest.updateEntity(changeEntry);
		
		ChangeEntry result = daoUnderTest.getEntity(changeEntry.getId(), ChangeEntry.class);
		
		assertTrue(result.isAccepted());
		assertFalse(result.isDeclined());
	}
	
	@Test
	void testDeclineChangeEntry() throws Exception {
		changeEntry.setDeclined(true);
		daoUnderTest.updateEntity(changeEntry);
		
		ChangeEntry result = daoUnderTest.getEntity(changeEntry.getId(), ChangeEntry.class);
		
		assertFalse(result.isAccepted());
		assertTrue(result.isDeclined());
	}
	
	@Test
	void testGetChangeList() throws Exception {
		ChangeEntry changeEntry1 = new ChangeEntry(false, false, 23.45, 2025, changeUser, createUser, "InfoText", "source: bild.de", Country.GERMANY, sourceEmissionEntry);
		getTransaction().begin();
		getEntityManager().persist(changeEntry1);
		getTransaction().commit();
		
		List<ChangeEntry> changeList = daoUnderTest.getChangeListByUser(createUser);
		assertEquals(2, changeList.size());
	}
}
