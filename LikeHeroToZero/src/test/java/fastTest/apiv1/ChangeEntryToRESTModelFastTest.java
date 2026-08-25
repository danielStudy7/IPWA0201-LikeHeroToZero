package fastTest.apiv1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;

import application.apiv1.mapper.AbstractMapper;
import application.apiv1.mapper.ChangeEntryToRESTModel;
import model.ChangeEntry;
import model.ChangeEntryRESTModel;
import model.Country;
import model.EmissionEntry;
import model.User;

public class ChangeEntryToRESTModelFastTest extends AbstractMapperTest<ChangeEntry, ChangeEntryRESTModel>{

	@Override
	protected AbstractMapper<ChangeEntry, ChangeEntryRESTModel> createMapper() {
		
		return new ChangeEntryToRESTModel();
	}

	@Override
	protected ChangeEntry createValidSource() {

		User user = new User("name", "pw");
		EmissionEntry emissionEntry = new EmissionEntry(Country.AFGHANISTAN, 11.22, 2026, true, user);
		emissionEntry.setId(UUID.randomUUID());
		ChangeEntry source = new ChangeEntry(false, false, 12.02, 2026, user, user, "Info", "Source", Country.AFGHANISTAN, emissionEntry);
		source.setId(UUID.randomUUID());
		
		return source;
	}

	@Override
	protected void assertMapped(ChangeEntry source, ChangeEntryRESTModel target) {
		
		assertEquals(source.getCountry(), target.getCountry());
		assertEquals(source.getEmissionEntry().getId().toString(), target.getEmissionEntryUUID());
		assertEquals(source.getEmissions(), target.getEmissions());
		assertEquals(source.getInfoText(), target.getInfoText());
		assertEquals(source.getSource(), target.getSource());
		assertEquals(source.getId().toString(), target.getUuid());
		assertEquals(source.getYear(), target.getYear());
		assertFalse(target.isAccepted());
		assertFalse(target.isDeclined());
	}

}
