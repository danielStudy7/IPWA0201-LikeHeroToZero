package test;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.primefaces.event.SelectEvent;

import controller.ChangesController;
import controller.UserSessionController;
import dao.ChangeEntryDAO;
import dao.EmissionEntryDAO;
import model.ChangeEntry;
import model.EmissionEntry;
import model.User;

@ExtendWith(EasyMockExtension.class)
class ChangesControllerFastTest {

	@TestSubject
	private ChangesController controllerUnderTest;
	
	@Mock
	private UserSessionController userSessionController;
	@Mock
	private ChangeEntryDAO changeEntryDao;
	@Mock
	private EmissionEntryDAO emissionEntryDao;
	
	private User changeUser;
	private EmissionEntry emissionEntryGermany;
	private EmissionEntry emissionEntrySpain;
	private ChangeEntry changeEntryGermany;
	private ChangeEntry changeEntrySpain;
	private List<ChangeEntry> changeEntryList;
	
	@BeforeEach
	public void setUp() throws Exception {
		changeUser = new User("changer", "secret");
		
		emissionEntryGermany = new EmissionEntry("Germany", 20.2, 2025, false, changeUser);
		emissionEntrySpain = new EmissionEntry("Spain", 22.3, 2026, false, changeUser);
		
		changeEntryGermany = new ChangeEntry(false, false, 20.2, 2025, changeUser, changeUser, "InfoText", "bild.de", "Germany", emissionEntryGermany);
		changeEntrySpain = new ChangeEntry(false, false, 22.2, 2025, changeUser, changeUser, "InfoText", "marca.de", "Spain", emissionEntrySpain);
		
		changeEntryList = Arrays.asList(changeEntryGermany, changeEntrySpain);
	}
	
	@Test
	public void testAcceptChange_NoChange() throws Exception {
		assertNull(controllerUnderTest.getSelectedChangeEntry());
		
		controllerUnderTest.acceptChange();
		
		assertFalse(changeEntryList.get(0).isAccepted());
		assertFalse(changeEntryList.get(1).isAccepted());
	}
	
	@Test
	public void testAcceptChange_AcceptWithoutChanges() throws Exception {
		SelectEvent<ChangeEntry> event = createMock("event", SelectEvent.class);
		
		controllerUnderTest.setSelectedChangeEntry(changeEntryGermany);
		assertNotNull(controllerUnderTest.getSelectedChangeEntry());
		
		reset(userSessionController, changeEntryDao, emissionEntryDao, event);
		expect(event.getObject()).andReturn(changeEntryGermany).times(2);
		changeEntryDao.acceptChangeEntry(changeEntryGermany);
		expectLastCall();
		emissionEntryDao.updateEmissionEntry(emissionEntryGermany);
		expectLastCall();
		replay(userSessionController, changeEntryDao, emissionEntryDao, event);
		
		controllerUnderTest.onRowSelect(event);
		controllerUnderTest.acceptChange();
		
		assertEquals(changeEntryGermany.getCountry(), emissionEntryGermany.getCountry());
		assertEquals(changeEntryGermany.getEmissions(), emissionEntryGermany.getEmissions());
		assertEquals(changeEntryGermany.getEmissionEntry().getId(), emissionEntryGermany.getId());
		assertEquals(changeEntryGermany.getCreateUser(), emissionEntryGermany.getUser());
		assertEquals(changeEntryGermany.getYear(), emissionEntryGermany.getYear());
		
		verify(userSessionController, changeEntryDao, emissionEntryDao, event);
	}

	@Test
	public void testAcceptChange_AcceptWithChanges() throws Exception {
		SelectEvent<ChangeEntry> event = createMock("event", SelectEvent.class);
		
		controllerUnderTest.setSelectedChangeEntry(changeEntrySpain);
		assertNotNull(controllerUnderTest.getSelectedChangeEntry());
		
		reset(userSessionController, changeEntryDao, emissionEntryDao, event);
		expect(event.getObject()).andReturn(changeEntrySpain).times(2);
		changeEntryDao.acceptChangeEntry(changeEntrySpain);
		expectLastCall();
		emissionEntryDao.updateEmissionEntry(emissionEntrySpain);
		expectLastCall();
		replay(userSessionController, changeEntryDao, emissionEntryDao, event);
		
		controllerUnderTest.onRowSelect(event);
		controllerUnderTest.acceptChange();
		
		assertEquals(changeEntrySpain.getCountry(), emissionEntrySpain.getCountry());
		assertEquals(changeEntrySpain.getEmissions(), emissionEntrySpain.getEmissions());
		assertEquals(changeEntrySpain.getEmissionEntry().getId(), emissionEntrySpain.getId());
		assertEquals(changeEntrySpain.getCreateUser(), emissionEntrySpain.getUser());
		assertEquals(changeEntrySpain.getYear(), emissionEntrySpain.getYear());
		
		verify(userSessionController, changeEntryDao, emissionEntryDao, event);
	}
	
	@Test
	public void testDeclineChange() throws Exception {
		SelectEvent<ChangeEntry> event = createMock("event", SelectEvent.class);
		
		controllerUnderTest.setSelectedChangeEntry(changeEntrySpain);
		assertNotNull(controllerUnderTest.getSelectedChangeEntry());
		
		reset(userSessionController, changeEntryDao, emissionEntryDao, event);
		expect(event.getObject()).andReturn(changeEntrySpain).times(2);
		changeEntryDao.declineChangeEntry(changeEntrySpain);
		expectLastCall();
		replay(userSessionController, changeEntryDao, emissionEntryDao, event);
		
		controllerUnderTest.onRowSelect(event);
		controllerUnderTest.declineChange();
		
		assertFalse(changeEntrySpain.isAccepted());
	}
}
