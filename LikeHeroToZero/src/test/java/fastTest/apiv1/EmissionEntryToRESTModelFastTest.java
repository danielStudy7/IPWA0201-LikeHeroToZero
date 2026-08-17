package fastTest.apiv1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import application.apiv1.mapper.AbstractMapper;
import application.apiv1.mapper.EmissionEntryToRESTModel;
import application.apiv1.model.EmissionEntryRESTModel;
import model.Country;
import model.EmissionEntry;
import model.User;

public class EmissionEntryToRESTModelFastTest extends AbstractMapperTest<EmissionEntry, EmissionEntryRESTModel>{

	@Override
	protected AbstractMapper<EmissionEntry, EmissionEntryRESTModel> createMapper() {
		
		return new EmissionEntryToRESTModel();
	}

	@Override
	protected EmissionEntry createValidSource() {
		
		EmissionEntry source = new EmissionEntry();
		source.setId(UUID.randomUUID());
		source.setYear(2026);
		source.setChecked(true);
		source.setEmissions(22.22);
		source.setCountry(Country.GERMANY);
		
		User user = new User("name", "pw");
		source.setUser(user);
		
		return source;
	}

	@Override
	protected void assertMapped(EmissionEntry source, EmissionEntryRESTModel target) {

		assertEquals(source.getId().toString(), target.getId());
		assertEquals(source.getYear(), target.getYear());
		assertEquals(source.isChecked(), target.isChecked());
		assertEquals(source.getEmissions(), target.getEmissions());
	}
}
