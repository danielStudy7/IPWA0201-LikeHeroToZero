package fastTest;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.primefaces.event.SelectEvent;

import common.FailedOperationException;
import controller.BackendController;
import controller.UserSessionController;
import jakarta.faces.validator.ValidatorException;
import model.ChangeEntry;
import model.Country;
import model.EmissionEntry;
import model.User;
import service.ChangeEntryService;

@ExtendWith(EasyMockExtension.class)
public class BackendControllerFastTest {

    @TestSubject
    private BackendController controllerUnderTest = new BackendController();

    @Mock
    private UserSessionController userSession;
    @Mock
    private ChangeEntryService changeEntryService;

    private User user;
    private EmissionEntry emissionEntrySpain;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(100);
        user.setUserName("dhirt");
        user.setPassword("geheim");

        emissionEntrySpain = new EmissionEntry();
        emissionEntrySpain.setId(10);
        emissionEntrySpain.setCountry(Country.SPAIN_AND_ANDORRA);
        emissionEntrySpain.setEmissions(22.3);
        emissionEntrySpain.setYear(2023);
        emissionEntrySpain.setUser(user);
    }

    @Test
    public void testConstructor_InitialState() {
        assertNotNull(controllerUnderTest.getChangeEntry());
        assertNotNull(controllerUnderTest.getLazyDataModel());
        assertNull(controllerUnderTest.getSelectedEmissionEntry());
        assertNull(controllerUnderTest.getEmissionEntry());
    }

    @Test
    public void testOnRowSelect_SetsSelectedEmissionEntry() {
        SelectEvent<EmissionEntry> event = createMock("event", SelectEvent.class);

        reset(userSession, changeEntryService, event);
        expect(event.getObject()).andReturn(emissionEntrySpain);
        replay(userSession, changeEntryService, event);

        controllerUnderTest.onRowSelect(event);

        assertSame(emissionEntrySpain, controllerUnderTest.getSelectedEmissionEntry());

        verify(userSession, changeEntryService, event);
    }

    @Test
    public void testCreateChangeEntry_WithSelectedEntry() throws FailedOperationException {
        SelectEvent<EmissionEntry> event = createMock("event", SelectEvent.class);
        ChangeEntry changeEntryBefore = controllerUnderTest.getChangeEntry();
 
        reset(userSession, changeEntryService, event);
        expect(event.getObject()).andReturn(emissionEntrySpain);
        expect(userSession.getCurrentUser()).andReturn(user);
        changeEntryService.createChangeEntry(changeEntryBefore, emissionEntrySpain, user);
        expectLastCall();
        replay(userSession, changeEntryService, event);
 
        controllerUnderTest.onRowSelect(event);
        controllerUnderTest.createChangeEntry();
 
        assertNull(controllerUnderTest.getSelectedEmissionEntry());
        assertNotNull(controllerUnderTest.getChangeEntry());
        assertNotSame(changeEntryBefore, controllerUnderTest.getChangeEntry());
 
        verify(userSession, changeEntryService, event);
    }
    @Test
    public void testCreateChangeEntry_WithoutSelectedEntry() throws FailedOperationException {
        ChangeEntry changeEntryBefore = controllerUnderTest.getChangeEntry();
        assertNull(controllerUnderTest.getSelectedEmissionEntry());

        reset(userSession, changeEntryService);
        replay(userSession, changeEntryService);

        ValidatorException exception = assertThrows(ValidatorException.class,
                () -> controllerUnderTest.createChangeEntry());

        assertNotNull(exception.getFacesMessage());
        assertEquals("Bitte wählen Sie einen Eintrag zum Ändern aus.",
                exception.getFacesMessage().getSummary());

        assertSame(changeEntryBefore, controllerUnderTest.getChangeEntry());
        assertNull(controllerUnderTest.getSelectedEmissionEntry());

        verify(userSession, changeEntryService);
    }

    @Test
    public void testCreateChangeEntry_ServiceThrowsException() throws FailedOperationException {
        ChangeEntry changeEntryBefore = controllerUnderTest.getChangeEntry();
        controllerUnderTest.setSelectedEmissionEntry(emissionEntrySpain);

        reset(userSession, changeEntryService);
        expect(userSession.getCurrentUser()).andReturn(user);
        changeEntryService.createChangeEntry(changeEntryBefore, emissionEntrySpain, user);
        expectLastCall().andThrow(new FailedOperationException("create failed"));
        replay(userSession, changeEntryService);

        assertThrows(FailedOperationException.class, () -> controllerUnderTest.createChangeEntry());

        assertSame(changeEntryBefore, controllerUnderTest.getChangeEntry());
        assertSame(emissionEntrySpain, controllerUnderTest.getSelectedEmissionEntry());

        verify(userSession, changeEntryService);
    }

    @Test
    public void testSetSelectedEmissionEntry() {
        controllerUnderTest.setSelectedEmissionEntry(emissionEntrySpain);
        assertSame(emissionEntrySpain, controllerUnderTest.getSelectedEmissionEntry());

        controllerUnderTest.setSelectedEmissionEntry(null);
        assertNull(controllerUnderTest.getSelectedEmissionEntry());
    }

    @Test
    public void testEdit_DoesNothing() {
        ChangeEntry changeEntryBefore = controllerUnderTest.getChangeEntry();

        reset(userSession, changeEntryService);
        replay(userSession, changeEntryService);

        controllerUnderTest.edit();

        assertSame(changeEntryBefore, controllerUnderTest.getChangeEntry());
        assertNull(controllerUnderTest.getSelectedEmissionEntry());

        verify(userSession, changeEntryService);
    }
}