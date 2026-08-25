package fastTest.apiv1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;

import application.apiv1.mapper.AbstractMapper;
import application.apiv1.mapper.ChangeEntryCreateRESTModelToChangeEntry;
import model.ChangeEntry;
import model.ChangeEntryCreateRESTModel;
import model.Country;

public class ChangeEntryCreateRESTModelToChangeEntryFastTest extends AbstractMapperTest<ChangeEntryCreateRESTModel, ChangeEntry> {

	@Override
	protected AbstractMapper<ChangeEntryCreateRESTModel, ChangeEntry> createMapper() {
		return new ChangeEntryCreateRESTModelToChangeEntry();
	}

	@Override
	protected ChangeEntryCreateRESTModel createValidSource() {

		ChangeEntryCreateRESTModel source = new ChangeEntryCreateRESTModel();
		source.setCountry(Country.AFGHANISTAN);
		source.setEmissionEntryUUID(UUID.randomUUID().toString());
		source.setEmissions(44.44);
		source.setInfoText("INFO");
		source.setSource("SOURCE");
		source.setYear(2026);
		
		return source;
	}

	@Override
	protected void assertMapped(ChangeEntryCreateRESTModel source, ChangeEntry target) {

		assertEquals(source.getCountry(), target.getCountry());
		assertEquals(source.getEmissions(), target.getEmissions());
		assertEquals(source.getInfoText(), target.getInfoText());
		assertEquals(source.getSource(), target.getSource());
		assertEquals(source.getYear(), target.getYear());
		assertFalse(target.isAccepted());
		assertFalse(target.isDeclined());
	} 


}
