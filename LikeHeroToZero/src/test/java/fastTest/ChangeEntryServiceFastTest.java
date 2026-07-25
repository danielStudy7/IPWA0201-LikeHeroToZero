package fastTest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import common.FailedOperationException;
import dao.ChangeEntryDAO;
import dao.EmissionEntryDAO;
import model.ChangeEntry;
import model.Country;
import model.EmissionEntry;
import model.User;
import service.ChangeEntryService;

@ExtendWith(EasyMockExtension.class)
public class ChangeEntryServiceFastTest {

    @TestSubject
    private ChangeEntryService serviceUnderTest = new ChangeEntryService();

    @Mock
    private ChangeEntryDAO changeEntryDao;
    @Mock
    private EmissionEntryDAO emissionEntryDao;

    private User createUser;
    private User changeUser;
    private EmissionEntry emissionEntrySpain;
    private ChangeEntry changeEntrySpain;

    @BeforeEach
    public void setUp() {
        createUser = new User();
        createUser.setId(100);
        createUser.setUserName("dhirt");
        createUser.setPassword("geheim");

        changeUser = new User();
        changeUser.setId(200);
        changeUser.setUserName("mmuster");
        changeUser.setPassword("geheim");

        emissionEntrySpain = new EmissionEntry();
        emissionEntrySpain.setId(10);
        emissionEntrySpain.setCountry(Country.SPAIN_AND_ANDORRA);
        emissionEntrySpain.setEmissions(22.3);
        emissionEntrySpain.setYear(2023);
        emissionEntrySpain.setUser(createUser);

        changeEntrySpain = new ChangeEntry();
        changeEntrySpain.setId(1);
    }

    @Test
    public void testCreateChangeEntry_EmptyFieldsTakenFromEmissionEntry() throws FailedOperationException {
        reset(changeEntryDao, emissionEntryDao);
        changeEntryDao.createEntity(changeEntrySpain);
        expectLastCall();
        replay(changeEntryDao, emissionEntryDao);

        serviceUnderTest.createChangeEntry(changeEntrySpain, emissionEntrySpain, changeUser);

        assertEquals(Country.SPAIN_AND_ANDORRA, changeEntrySpain.getCountry());
        assertEquals(22.3, changeEntrySpain.getEmissions());
        assertEquals(2023, changeEntrySpain.getYear());

        assertFalse(changeEntrySpain.isAccepted());
        assertFalse(changeEntrySpain.isDeclined());
        assertSame(emissionEntrySpain, changeEntrySpain.getEmissionEntry());
        assertSame(changeUser, changeEntrySpain.getChangeUser());
        assertSame(createUser, changeEntrySpain.getCreateUser());

        verify(changeEntryDao, emissionEntryDao);
    }

    @Test
    public void testCreateChangeEntry_ExistingFieldsNotOverwritten() throws FailedOperationException {
        changeEntrySpain.setCountry(Country.PORTUGAL);
        changeEntrySpain.setEmissions(22.2);
        changeEntrySpain.setYear(2024);

        reset(changeEntryDao, emissionEntryDao);
        changeEntryDao.createEntity(changeEntrySpain);
        expectLastCall();
        replay(changeEntryDao, emissionEntryDao);

        serviceUnderTest.createChangeEntry(changeEntrySpain, emissionEntrySpain, changeUser);

        assertEquals(Country.PORTUGAL, changeEntrySpain.getCountry());
        assertEquals(22.2, changeEntrySpain.getEmissions());
        assertEquals(2024, changeEntrySpain.getYear());

        verify(changeEntryDao, emissionEntryDao);
    }

    @Test
    public void testCreateChangeEntry_FlagsAlwaysReset() throws FailedOperationException {
        changeEntrySpain.setAccepted(true);
        changeEntrySpain.setDeclined(true);

        reset(changeEntryDao, emissionEntryDao);
        changeEntryDao.createEntity(changeEntrySpain);
        expectLastCall();
        replay(changeEntryDao, emissionEntryDao);

        serviceUnderTest.createChangeEntry(changeEntrySpain, emissionEntrySpain, changeUser);

        assertFalse(changeEntrySpain.isAccepted());
        assertFalse(changeEntrySpain.isDeclined());

        verify(changeEntryDao, emissionEntryDao);
    }

    @Test
    public void testCreateChangeEntry_DaoThrowsException() throws FailedOperationException {
        reset(changeEntryDao, emissionEntryDao);
        changeEntryDao.createEntity(changeEntrySpain);
        expectLastCall().andThrow(new FailedOperationException("create failed"));
        replay(changeEntryDao, emissionEntryDao);

        FailedOperationException exception = assertThrows(FailedOperationException.class,
                () -> serviceUnderTest.createChangeEntry(changeEntrySpain, emissionEntrySpain, changeUser));

        assertNotNull(exception);
        verify(changeEntryDao, emissionEntryDao);
    }

    @Test
    public void testAcceptChange_Accepted() throws FailedOperationException {
        changeEntrySpain.setCountry(Country.PORTUGAL);
        changeEntrySpain.setEmissions(22.2);
        changeEntrySpain.setYear(2024);

        reset(changeEntryDao, emissionEntryDao);
        changeEntryDao.updateEntity(changeEntrySpain);
        expectLastCall();
        emissionEntryDao.updateEntity(emissionEntrySpain);
        expectLastCall();
        replay(changeEntryDao, emissionEntryDao);

        serviceUnderTest.acceptChange(changeEntrySpain, emissionEntrySpain, true);

        assertTrue(changeEntrySpain.isAccepted());
        assertFalse(changeEntrySpain.isDeclined());

        assertEquals(Country.PORTUGAL, emissionEntrySpain.getCountry());
        assertEquals(22.2, emissionEntrySpain.getEmissions());
        assertEquals(2024, emissionEntrySpain.getYear());

        verify(changeEntryDao, emissionEntryDao);
    }

    @Test
    public void testAcceptChange_Declined() throws FailedOperationException {
        changeEntrySpain.setCountry(Country.PORTUGAL);
        changeEntrySpain.setEmissions(22.2);
        changeEntrySpain.setYear(2024);

        reset(changeEntryDao, emissionEntryDao);
        changeEntryDao.updateEntity(changeEntrySpain);
        expectLastCall();
        emissionEntryDao.updateEntity(emissionEntrySpain);
        expectLastCall();
        replay(changeEntryDao, emissionEntryDao);

        serviceUnderTest.acceptChange(changeEntrySpain, emissionEntrySpain, false);

        assertTrue(changeEntrySpain.isDeclined());
        assertFalse(changeEntrySpain.isAccepted());

        assertEquals(Country.PORTUGAL, emissionEntrySpain.getCountry());
        assertEquals(22.2, emissionEntrySpain.getEmissions());
        assertEquals(2024, emissionEntrySpain.getYear());

        verify(changeEntryDao, emissionEntryDao);
    }

    @Test
    public void testAcceptChange_ChangeEntryDaoThrowsException() throws FailedOperationException {
        reset(changeEntryDao, emissionEntryDao);
        changeEntryDao.updateEntity(changeEntrySpain);
        expectLastCall().andThrow(new FailedOperationException("update failed"));
        replay(changeEntryDao, emissionEntryDao);

        assertThrows(FailedOperationException.class,
                () -> serviceUnderTest.acceptChange(changeEntrySpain, emissionEntrySpain, true));

        assertEquals(Country.SPAIN_AND_ANDORRA, emissionEntrySpain.getCountry());
        assertEquals(22.3, emissionEntrySpain.getEmissions());
        assertEquals(2023, emissionEntrySpain.getYear());

        verify(changeEntryDao, emissionEntryDao);
    }

    @Test
    public void testAcceptChange_EmissionEntryDaoThrowsException() throws FailedOperationException {
        reset(changeEntryDao, emissionEntryDao);
        changeEntryDao.updateEntity(changeEntrySpain);
        expectLastCall();
        emissionEntryDao.updateEntity(emissionEntrySpain);
        expectLastCall().andThrow(new FailedOperationException("update failed"));
        replay(changeEntryDao, emissionEntryDao);

        assertThrows(FailedOperationException.class,
                () -> serviceUnderTest.acceptChange(changeEntrySpain, emissionEntrySpain, true));

        assertTrue(changeEntrySpain.isAccepted());

        verify(changeEntryDao, emissionEntryDao);
    }

    @Test
    public void testGetChangesForUser_ReturnsDaoList() {
        List<ChangeEntry> expectedList = Arrays.asList(changeEntrySpain);

        reset(changeEntryDao, emissionEntryDao);
        expect(changeEntryDao.getChangeListByUser(createUser)).andReturn(expectedList);
        replay(changeEntryDao, emissionEntryDao);

        List<ChangeEntry> result = serviceUnderTest.getChangesForUser(createUser);

        assertSame(expectedList, result);
        assertEquals(1, result.size());
        assertSame(changeEntrySpain, result.get(0));

        verify(changeEntryDao, emissionEntryDao);
    }

    @Test
    public void testGetChangesForUser_EmptyList() {
        reset(changeEntryDao, emissionEntryDao);
        expect(changeEntryDao.getChangeListByUser(changeUser)).andReturn(new ArrayList<ChangeEntry>());
        replay(changeEntryDao, emissionEntryDao);

        List<ChangeEntry> result = serviceUnderTest.getChangesForUser(changeUser);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(changeEntryDao, emissionEntryDao);
    }
}