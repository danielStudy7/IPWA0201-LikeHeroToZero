package test;


import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import controller.BackendController;
import controller.UserSessionController;
import dao.ChangeEntryDAO;
import lazyDataModel.LazyEmissionEntryDataModel;
import model.ChangeEntry;

@ExtendWith(EasyMockExtension.class)
class BackendControllerFastTest {

	@TestSubject
	private BackendController controllerUnderTest;
	
	@Mock
	private ChangeEntryDAO changeEntryDao;
	@Mock
	private UserSessionController userSessionController;
	@Mock
	private LazyEmissionEntryDataModel lazyDataModel;
	@Mock
	private ChangeEntry changeEntry;
	
//	private User user;
//	private EmissionEntry emissionEntry;
//	private ChangeEntry changeEntryFilled;
//	private ChangeEntry emptyChangeEntry;
	
	@BeforeEach
	public void setUp() throws Exception {
//		user = new User("test", "secret");
//		emissionEntry = new EmissionEntry("Germany", 20.2, 2025, false, user);
//		changeEntryFilled = new ChangeEntry(false, false, 24.2, 2025, user, user, "InfoText", "bild.de", "Germany", emissionEntry);
//		emptyChangeEntry = new ChangeEntry(false, false, 0.0, 0, user, user, "InfoText", "bild.de", "", emissionEntry);
	}
	
	@Test
	public void testCreateChangeEntry() throws Exception {
		// Test erst wenn Refactoring durch ist
	}
	
}
