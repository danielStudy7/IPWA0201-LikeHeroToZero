package fastTest;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import common.FailedOperationException;
import controller.NewEntryController;
import controller.UserSessionController;
import dao.EmissionEntryDAO;
import dao.UserDAO;
import model.EmissionEntry;
import model.User;
import service.UserService;

@ExtendWith(EasyMockExtension.class)
public class NewEntryControllerFastTest {

    @TestSubject
    private NewEntryController controllerUnderTest = new NewEntryController();

    @Mock
    private EmissionEntryDAO emissionDao;
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
        user.setId(100);
        user.setUserName("dhirt");
        user.setPassword("geheim");
    }

    @Test
    public void testCreateEmissionEntry_Success() throws FailedOperationException {
        EmissionEntry entry = controllerUnderTest.getEmissionEntry();

        reset(emissionDao, userSession, userService);
        expect(userSession.getCurrentUser()).andReturn(user).times(2);
        emissionDao.createEntity(entry);
        expectLastCall();
        userService.updateUser(user);
        expectLastCall();
        replay(emissionDao, userSession, userService);

        controllerUnderTest.createEmissionEntry();

        assertTrue(entry.isChecked());
        assertEquals(user, entry.getUser());
        assertNotSame(entry, controllerUnderTest.getEmissionEntry());

        verify(emissionDao, userSession, userService);
    }
}