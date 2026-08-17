package slowTest.apiv1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.Binder;
import org.junit.jupiter.api.Test;

import application.apiv1.model.EmissionEntryCreateRESTModel;
import application.apiv1.model.EmissionEntryRESTModel;
import dao.EmissionEntryDAO;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import model.Country;
import model.EmissionEntry;
import model.User;
import service.EmissionEntryService;

public class EmissionOperationsSlowTest extends AbstractJerseySlowTestVorlage {

	private EmissionEntry emissionEntry;

	@Override
	protected Binder createTestBinder() {

		return new AbstractBinder() {
			
			@Override
			protected void configure() {
				bindFactory(() -> new EmissionEntryService(new EmissionEntryDAO(getEntityManager())))//
					.to(EmissionEntryService.class);
			}
		};
	}

	@Override
	public void createTestData() {
		
		User user = new User("user", "pw");
		getEntityManager().persist(user);
		
		emissionEntry = new EmissionEntry(Country.GERMANY, 45.55, 2026, true, user);
		getEntityManager().persist(emissionEntry);
	}

	@Test
	public void testGetAllEmissions() throws Exception {
		
		try (Response response = httpGetMethod("/emissions/getAllEmissions")) {
			
			assertEquals(200, response.getStatus());
			
			List<EmissionEntryRESTModel> result = response.readEntity(new GenericType<List<EmissionEntryRESTModel>>() {});
			
			assertEquals(1, result.size());
			
			EmissionEntryRESTModel emissionEntryRESTModel = result.get(0);
			assertEquals(emissionEntry.getEmissions(), emissionEntryRESTModel.getEmissions());
			assertEquals(emissionEntry.getYear(), emissionEntryRESTModel.getYear());
			assertEquals(emissionEntry.getId().toString(), emissionEntryRESTModel.getId());
			assertEquals(emissionEntry.isChecked(), emissionEntryRESTModel.isChecked());
			assertEquals(emissionEntry.getCountry(), emissionEntryRESTModel.getCountry());
			
		}
	}
	
	@Test
	public void testCreateEmissionEntry() throws Exception {
		
		EmissionEntryCreateRESTModel model = new EmissionEntryCreateRESTModel();
		model.setChecked(true);
		model.setCountry(Country.GERMANY);
		model.setEmissions(0.789);
		model.setYear(2026);
		
		try(Response response = httpPostMethod("/emissions/createEmissionEntry", model)) {
			
			assertEquals(200, response.getStatus());
			
			EmissionEntryRESTModel result = response.readEntity(EmissionEntryRESTModel.class);
			assertEquals(result.getCountry(), Country.GERMANY);
			
		}
	}
}
