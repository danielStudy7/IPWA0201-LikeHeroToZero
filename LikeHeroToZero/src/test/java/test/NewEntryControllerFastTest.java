package test;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import controller.NewEntryController;
import controller.UserSessionController;
import dao.EmissionEntryDAO;
import dao.UserDAO;
import model.EmissionEntry;
import model.User;

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

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(100);
        user.setUserName("dhirt");
        user.setPassword("geheim");
    }

    @Test
    public void testCreateEmissionEntry_Success() {
        EmissionEntry entry = controllerUnderTest.getEmissionEntry();

        reset(emissionDao, userDao, userSession);
        expect(userSession.getCurrentUser()).andReturn(user).times(2);
        userDao.updateUser(user);
        expectLastCall();
        emissionDao.createEmissionEntry(entry);
        expectLastCall();
        replay(emissionDao, userDao, userSession);

        controllerUnderTest.createEmissionEntry();

        assertTrue(entry.isChecked());
        assertEquals(user, entry.getUser());
        assertNotSame(entry, controllerUnderTest.getEmissionEntry());

        verify(emissionDao, userDao, userSession);
    }
}