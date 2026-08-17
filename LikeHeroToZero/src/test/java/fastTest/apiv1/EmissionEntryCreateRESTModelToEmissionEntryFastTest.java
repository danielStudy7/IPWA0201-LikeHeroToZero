package fastTest.apiv1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import application.apiv1.mapper.AbstractMapper;
import application.apiv1.mapper.EmissionEntryCreateRESTModelToEmissionEntry;
import application.apiv1.model.EmissionEntryCreateRESTModel;
import model.Country;
import model.EmissionEntry;

public class EmissionEntryCreateRESTModelToEmissionEntryFastTest extends AbstractMapperTest<EmissionEntryCreateRESTModel, EmissionEntry>{

	@Override
	protected AbstractMapper<EmissionEntryCreateRESTModel, EmissionEntry> createMapper() {
		return new EmissionEntryCreateRESTModelToEmissionEntry();
	}

	@Override
	protected EmissionEntryCreateRESTModel createValidSource() {
		EmissionEntryCreateRESTModel source = new EmissionEntryCreateRESTModel();
		source.setChecked(true);
		source.setCountry(Country.GERMANY);
		source.setEmissions(22.22);
		source.setYear(2026);
		
		return source;
	}

	@Override
	protected void assertMapped(EmissionEntryCreateRESTModel source, EmissionEntry target) {
		
		assertEquals(source.getYear(), target.getYear());
		assertEquals(source.isChecked(), target.isChecked());
		assertEquals(source.getEmissions(), target.getEmissions());
		assertEquals(source.getCountry(), target.getCountry());
	}
}
