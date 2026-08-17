package fastTest;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import common.FailedOperationException;
import controller.NewEntryController;
import controller.UserSessionController;
import dao.UserDAO;
import model.EmissionEntry;
import model.User;
import service.EmissionEntryService;
import service.UserService;

@ExtendWith(EasyMockExtension.class)
public class NewEntryControllerFastTest {

    @TestSubject
    private NewEntryController controllerUnderTest = new NewEntryController();

    @Mock
    private EmissionEntryService emissionEntryService;
    @Mock
    private UserDAO userDao;
    @Mock
    private UserSessionController userSession;
    @Mock
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUserName("dhirt");
        user.setPassword("geheim");
    }

    @Test
    public void testCreateEmissionEntry_Success() throws FailedOperationException {
        EmissionEntry entry = controllerUnderTest.getEmissionEntry();

        reset(emissionEntryService, userSession, userService);
        expect(userSession.getCurrentUser()).andReturn(user).times(2);
        emissionEntryService.createEmissionEntry(entry, user);
        expectLastCall();
        userService.updateUser(user);
        expectLastCall();
        entry.setChecked(true);
        expectLastCall();
        entry.setUser(user);
        expectLastCall();
        replay(emissionEntryService, userSession, userService);

        controllerUnderTest.createEmissionEntry();

        assertEquals(user, entry.getUser());
        assertTrue(entry.isChecked());
        assertNotSame(entry, controllerUnderTest.getEmissionEntry());

        verify(emissionEntryService, userSession, userService);
    }
}