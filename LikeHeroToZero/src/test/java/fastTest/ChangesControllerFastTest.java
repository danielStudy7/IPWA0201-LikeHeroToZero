package fastTest;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import jakarta.faces.validator.ValidatorException;
import model.ChangeEntry;
import model.Country;
import model.EmissionEntry;
import model.User;
import service.ChangeEntryService;

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
	@Mock
	private ChangeEntryService changeEntryService;
	
	private User changeUser;
	private EmissionEntry emissionEntryGermany;
	private EmissionEntry emissionEntrySpain;
	private ChangeEntry changeEntryGermany;
	private ChangeEntry changeEntrySpain;
	private List<ChangeEntry> changeEntryList;
	
	@BeforeEach
	public void setUp() throws Exception {
		changeUser = new User("changer", "secret");
		
		emissionEntryGermany = new EmissionEntry(Country.GERMANY, 20.2, 2025, false, changeUser);
		emissionEntrySpain = new EmissionEntry(Country.SPAIN_AND_ANDORRA, 22.3, 2026, false, changeUser);
		
		changeEntryGermany = new ChangeEntry(false, false, 20.2, 2025, changeUser, changeUser, "InfoText", "bild.de", Country.GERMANY, emissionEntryGermany);
		changeEntrySpain = new ChangeEntry(false, false, 22.2, 2025, changeUser, changeUser, "InfoText", "marca.de", Country.SPAIN_AND_ANDORRA, emissionEntrySpain);
		
		changeEntryList = Arrays.asList(changeEntryGermany, changeEntrySpain);
	}
	
	@Test
	public void testAcceptChange_NoChange() throws Exception {
		assertNull(controllerUnderTest.getSelectedChangeEntry());
		
		assertThrows(ValidatorException.class, () -> controllerUnderTest.acceptChange());
		
		assertFalse(changeEntryList.get(0).isAccepted());
		assertFalse(changeEntryList.get(1).isAccepted());
	}
	
	@Test
	public void testAcceptChange_AcceptWithoutChanges() throws Exception {
		SelectEvent<ChangeEntry> event = createMock("event", SelectEvent.class);
		
		changeEntryGermany.setAccepted(true);
		controllerUnderTest.setSelectedChangeEntry(changeEntryGermany);
		assertNotNull(controllerUnderTest.getSelectedChangeEntry());
		
		reset(userSessionController, changeEntryService, event);
		expect(event.getObject()).andReturn(changeEntryGermany).times(1);
		changeEntryDao.updateEntity(changeEntryGermany);
		expectLastCall();
		emissionEntryDao.updateEntity(emissionEntryGermany);
		expectLastCall();
		changeEntryService.acceptChange(changeEntryGermany, emissionEntryGermany, true);
		expectLastCall();
		replay(userSessionController, changeEntryService, event);
		
		controllerUnderTest.onRowSelect(event);
		controllerUnderTest.acceptChange();
		
		assertNull(controllerUnderTest.getSelectedChangeEntry());
		assertNull(controllerUnderTest.getEmissionEntry());
		
		verify(userSessionController, changeEntryService, event);
	}

	@Test
	public void testAcceptChange_AcceptWithChanges() throws Exception {
		SelectEvent<ChangeEntry> event = createMock("event", SelectEvent.class);
		
		changeEntrySpain.setAccepted(true);
		controllerUnderTest.setSelectedChangeEntry(changeEntrySpain);
		assertNotNull(controllerUnderTest.getSelectedChangeEntry());
		
		reset(userSessionController, changeEntryService, event);
		expect(event.getObject()).andReturn(changeEntrySpain);
		changeEntryService.acceptChange(changeEntrySpain, emissionEntrySpain, true);
		expectLastCall();
		replay(userSessionController, changeEntryService, event);
		
		controllerUnderTest.onRowSelect(event);
		controllerUnderTest.acceptChange();
		
		assertNull(controllerUnderTest.getSelectedChangeEntry());
		assertNull(controllerUnderTest.getEmissionEntry());
		
		verify(userSessionController, changeEntryService, event);
	}
	
	@Test
	public void testDeclineChange() throws Exception {
		SelectEvent<ChangeEntry> event = createMock("event", SelectEvent.class);
		
		changeEntrySpain.setDeclined(true);
		controllerUnderTest.setSelectedChangeEntry(changeEntrySpain);
		assertNotNull(controllerUnderTest.getSelectedChangeEntry());
		
		reset(userSessionController, changeEntryService, emissionEntryDao, event);
		expect(event.getObject()).andReturn(changeEntrySpain).times(1);
		expectLastCall();
		changeEntryService.acceptChange(changeEntrySpain, emissionEntrySpain, false);
		expectLastCall();
		replay(userSessionController, changeEntryService, emissionEntryDao, event);
		
		controllerUnderTest.onRowSelect(event);
		controllerUnderTest.declineChange();
		
		assertFalse(changeEntrySpain.isAccepted());
		
		verify(userSessionController, changeEntryService, emissionEntryDao, event);
	}
}
