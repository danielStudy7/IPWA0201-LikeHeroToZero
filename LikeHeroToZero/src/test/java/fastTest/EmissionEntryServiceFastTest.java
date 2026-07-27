package fastTest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import dao.EmissionEntryDAO;
import model.Country;
import model.EmissionEntry;
import model.User;
import service.EmissionEntryService;

@ExtendWith(EasyMockExtension.class)
public class EmissionEntryServiceFastTest {
	
	@TestSubject
	private EmissionEntryService serviceUnderTest;
	
	@Mock
	private EmissionEntryDAO emissionEntryDao;
	
	@Test
	public void testCreateEmissionEntry() throws Exception {
		User user = new User("test", "Pw");
		EmissionEntry entry = new EmissionEntry(Country.ALBANIA, 22.22, 2026, false, user);
		
		reset(emissionEntryDao);
		emissionEntryDao.createEntity(entry);
		expectLastCall();
		replay(emissionEntryDao);
		
		serviceUnderTest.createEmissionEntry(entry, user);
		
		verify(emissionEntryDao);
	}
	
	@Test
	public void testFindAll() throws Exception {
		User user = new User("test", "Pw");
		EmissionEntry entry = new EmissionEntry(Country.ALBANIA, 22.22, 2026, false, user);
		EmissionEntry entry1 = new EmissionEntry(Country.ALBANIA, 22.22, 2026, false, user);
		
		reset(emissionEntryDao);
		expect(emissionEntryDao.findAll()).andReturn(Arrays.asList(entry, entry1));
		replay(emissionEntryDao);
		
		List<EmissionEntry> result = serviceUnderTest.findAll();
		assertEquals(2, result.size());
		
		verify(emissionEntryDao);
	}
}
