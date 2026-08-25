package slowTest.apiv1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.Binder;
import org.junit.jupiter.api.Test;

import dao.ChangeEntryDAO;
import dao.EmissionEntryDAO;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import model.ChangeEntry;
import model.ChangeEntryCreateRESTModel;
import model.ChangeEntryRESTModel;
import model.Country;
import model.EmissionEntry;
import model.UUIDListRESTModel;
import model.User;
import service.ChangeEntryService;

public class ChangeEntryOperaionsSlowTest extends AbstractJerseySlowTestVorlage {

	private EmissionEntry emissionEntrySpain2025;
	private User user;
	private EmissionEntry emissionEntryGermany2025;
	private EmissionEntry emissionEntrySpain2026;
	
	@Override
	protected Binder createTestBinder() {

		return new AbstractBinder() {
			
			@Override
			protected void configure() {
				bindFactory(() -> new ChangeEntryService(new ChangeEntryDAO(getEntityManager()), new EmissionEntryDAO(getEntityManager())))//
					.to(ChangeEntryService.class);
			}
		};
	}

	@Override
	public void createTestData() {
		
		user = new User("user", "pw");
		getEntityManager().persist(user);
		
		emissionEntrySpain2025 = new EmissionEntry(Country.SPAIN_AND_ANDORRA, 45.55, 2025, true, user);
		getEntityManager().persist(emissionEntrySpain2025);
		
		emissionEntryGermany2025 = new EmissionEntry(Country.GERMANY, 40.55, 2025, true, user);
		getEntityManager().persist(emissionEntryGermany2025);
		
		emissionEntrySpain2026 = new EmissionEntry(Country.SPAIN_AND_ANDORRA, 400, 2026, true, user);
		getEntityManager().persist(emissionEntrySpain2026);
	}

	@Test
	void testAcceptChangeEntry() throws Exception {
		
		ChangeEntry changeEntryGermany = new ChangeEntry(false, false, 10.01, 2025, user, user, "Etwas", "Nochwas", Country.GERMANY, emissionEntryGermany2025);
		getEntityManager().persist(changeEntryGermany);
		
		ChangeEntry changeEntrySpain = new ChangeEntry(false, false, 10.11, 2026, user, user, "Etwas", "Nochwas", Country.SPAIN_AND_ANDORRA, emissionEntrySpain2026);
		getEntityManager().persist(changeEntrySpain);
		
		UUIDListRESTModel model = new UUIDListRESTModel();
		model.setUuids(List.of(changeEntryGermany.getId().toString(), changeEntrySpain.getId().toString()));
		
		try (Response response = httpPostMethod("/changeEntry/accept", model, createToken(user))) {
			
			System.out.println(response.readEntity(String.class));
			
			assertEquals(204,  response.getStatus());
			
			ChangeEntry resultGermany = getDao().getEntity(changeEntryGermany.getId(), ChangeEntry.class);
			assertTrue(resultGermany.isAccepted());
			assertFalse(resultGermany.isDeclined());
			
			ChangeEntry resultSpain = getDao().getEntity(changeEntrySpain.getId(), ChangeEntry.class);
			assertTrue(resultSpain.isAccepted());
			assertFalse(resultSpain.isDeclined());
		}
	}
	
	@Test
	void testDeclineChangeEntry() throws Exception {
		ChangeEntry changeEntryGermany = new ChangeEntry(false, false, 10.01, 2025, user, user, "Etwas", "Nochwas", Country.GERMANY, emissionEntryGermany2025);
		getEntityManager().persist(changeEntryGermany);
		
		ChangeEntry changeEntrySpain = new ChangeEntry(false, false, 10.11, 2026, user, user, "Etwas", "Nochwas", Country.SPAIN_AND_ANDORRA, emissionEntrySpain2026);
		getEntityManager().persist(changeEntrySpain);
		
		UUIDListRESTModel model = new UUIDListRESTModel();
		model.setUuids(List.of(changeEntryGermany.getId().toString(), changeEntrySpain.getId().toString()));
		
		try (Response response = httpPostMethod("/changeEntry/decline", model, createToken(user))) {
			
			assertEquals(204,  response.getStatus());
			
			ChangeEntry resultGermany = getDao().getEntity(changeEntryGermany.getId(), ChangeEntry.class);
			assertFalse(resultGermany.isAccepted());
			assertTrue(resultGermany.isDeclined());
			
			ChangeEntry resultSpain = getDao().getEntity(changeEntrySpain.getId(), ChangeEntry.class);
			assertFalse(resultSpain.isAccepted());
			assertTrue(resultSpain.isDeclined());
		}
	}
	
	@Test
	void testListChangeEntrys() throws Exception {
		ChangeEntry changeEntryGermany = new ChangeEntry(false, false, 10.01, 2025, user, user, "Etwas", "Nochwas", Country.GERMANY, emissionEntryGermany2025);
		getEntityManager().persist(changeEntryGermany);
		
		ChangeEntry changeEntrySpain = new ChangeEntry(false, false, 10.11, 2026, user, user, "Etwas", "Nochwas", Country.SPAIN_AND_ANDORRA, emissionEntrySpain2026);
		getEntityManager().persist(changeEntrySpain);
		
		try (Response response = httpGetMethod("/changeEntry/list", createToken(user))) {
			assertEquals(200,  response.getStatus());
			
			List<ChangeEntryRESTModel> resultList = response.readEntity(new GenericType<List<ChangeEntryRESTModel>>() {});
			assertEquals(2, resultList.size());
		}
	}
	
	@Test
	void testCreateChangeEntry() throws Exception {

		String infoText = "Informationaaa";
		String source = "sourci";
		double emissions = 22.22;
		int year = 2026;
		
		ChangeEntryCreateRESTModel model = new ChangeEntryCreateRESTModel();
		model.setCountry(Country.GERMANY);
		model.setEmissions(emissions);
		model.setYear(year);
		model.setInfoText(infoText);
		model.setSource(source);
		model.setEmissionEntryUUID(emissionEntryGermany2025.getId().toString());
		
		try (Response response = httpPostMethod("/changeEntry/create", model, createToken(user))) {
			
			assertEquals(200, response.getStatus());
			
			ChangeEntryRESTModel result = response.readEntity(new GenericType<ChangeEntryRESTModel>() {});
			assertEquals(Country.GERMANY, result.getCountry());
			assertEquals(emissionEntryGermany2025.getId().toString(), result.getEmissionEntryUUID());
			assertEquals(22.22, result.getEmissions());
			assertEquals(infoText, result.getInfoText());
			assertEquals(source, result.getSource());
			assertEquals(year, result.getYear());
			assertNotNull(result.getUuid());
		}
	}
}
