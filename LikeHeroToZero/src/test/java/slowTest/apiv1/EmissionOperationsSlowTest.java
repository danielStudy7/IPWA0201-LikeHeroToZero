package slowTest.apiv1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.Binder;
import org.junit.jupiter.api.Test;

import application.apiv1.model.EmissionEntryCreateRESTModel;
import application.apiv1.model.EmissionEntryRESTModel;
import application.apiv1.model.EmissionPaginationResultRESTModel;
import application.apiv1.model.EmissionPaginationSearchRESTModel;
import application.apiv1.model.EmissionSortFieldRESTModel;
import dao.EmissionEntryDAO;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import model.Country;
import model.EmissionEntry;
import model.User;
import service.EmissionEntryService;

public class EmissionOperationsSlowTest extends AbstractJerseySlowTestVorlage {

	private EmissionEntry emissionEntryGermany2026;
	private EmissionEntry emissionEntryGermany2025;
	private EmissionEntry emissionEntrySpain2026;
	private EmissionEntry emissionEntrySpain2025;
	private User user;

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
		
		user = new User("user", "pw");
		getEntityManager().persist(user);
		
		emissionEntryGermany2026 = new EmissionEntry(Country.GERMANY, 45.55, 2026, true, user);
		getEntityManager().persist(emissionEntryGermany2026);
		
		emissionEntryGermany2025 = new EmissionEntry(Country.GERMANY, 40.55, 2025, true, user);
		getEntityManager().persist(emissionEntryGermany2025);
		
		emissionEntrySpain2026 = new EmissionEntry(Country.SPAIN_AND_ANDORRA, 400, 2026, true, user);
		getEntityManager().persist(emissionEntrySpain2026);
		
		emissionEntrySpain2025 = new EmissionEntry(Country.SPAIN_AND_ANDORRA, 399, 2025, true, user);
		getEntityManager().persist(emissionEntrySpain2025);
	}

	@Test
	public void testGetAllEmissions() throws Exception {
		
		try (Response response = httpGetMethod("/emissions/getAllEmissions")) {
			
			assertEquals(200, response.getStatus());
			
			List<EmissionEntryRESTModel> result = response.readEntity(new GenericType<List<EmissionEntryRESTModel>>() {});
			
			assertEquals(4, result.size());
		}
	}
	
	@Test
	public void testCreateEmissionEntry() throws Exception {
		
		EmissionEntryCreateRESTModel model = new EmissionEntryCreateRESTModel();
		model.setChecked(true);
		model.setCountry(Country.GERMANY);
		model.setEmissions(0.789);
		model.setYear(2026);
		
		try(Response response = httpPostMethod("/emissions/createEmissionEntry", model, createToken(user))) {
			
			assertEquals(200, response.getStatus());
			
			EmissionEntryRESTModel result = response.readEntity(EmissionEntryRESTModel.class);
			assertEquals(result.getCountry(), Country.GERMANY);
			assertEquals(result.getEmissions(), 0.789);
			assertEquals(result.getYear(), 2026);
			assertNotNull(result.getId());
		}
	}
	
	@Test
	public void testSearchEmissionEntrys() throws Exception {
		
		int year = 2025;
		
		EmissionPaginationSearchRESTModel searchRestModel = new EmissionPaginationSearchRESTModel();
		searchRestModel.setPage(0);
		searchRestModel.setEntriesPerPage(2);
		searchRestModel.setSortBy(EmissionSortFieldRESTModel.YEAR);
		searchRestModel.setSortDescending(false);
		
		try (Response response = httpPostMethod("/emissions/searchEmissionEntry", searchRestModel, null)) {
			
			assertEquals(200, response.getStatus());
			
			EmissionPaginationResultRESTModel result = response.readEntity(EmissionPaginationResultRESTModel.class);
			assertEquals(4, result.getTotalHits());
			
			List<EmissionEntryRESTModel> emissionEntrys = result.getEmissionEntrys();
			assertEquals(2, emissionEntrys.size());
			
			EmissionEntryRESTModel entry1 = emissionEntrys.get(0);
			assertEquals(year, entry1.getYear());
			
			EmissionEntryRESTModel entry2 = emissionEntrys.get(1);
			assertEquals(year, entry2.getYear());
		}
	}
	
	@Test
	public void testSearchEmissionEntrys_filterByYear() throws Exception {
		
		int filterYear = 2025;
		
		EmissionPaginationSearchRESTModel searchRestModel = new EmissionPaginationSearchRESTModel();
		searchRestModel.setPage(0);
		searchRestModel.setEntriesPerPage(2);
		searchRestModel.setSortBy(EmissionSortFieldRESTModel.YEAR);
		searchRestModel.setSortDescending(false);
		searchRestModel.setFilterYear(2025);
		
		try (Response response = httpPostMethod("/emissions/searchEmissionEntry", searchRestModel, null)) {
			
			assertEquals(200, response.getStatus());
			
			EmissionPaginationResultRESTModel result = response.readEntity(EmissionPaginationResultRESTModel.class);
			assertEquals(2, result.getTotalHits());
			
			List<EmissionEntryRESTModel> emissionEntrys = result.getEmissionEntrys();
			assertEquals(2, emissionEntrys.size());
			
			EmissionEntryRESTModel entrySpain2025 = emissionEntrys.get(0);
			assertEquals(filterYear, entrySpain2025.getYear());
			
			EmissionEntryRESTModel entryGermany2025 = emissionEntrys.get(1);
			assertEquals(filterYear, entryGermany2025.getYear());
		}
	}
	
	@Test
	public void testSearchEmissionEntrys_filterByCountry() throws Exception {
		
		EmissionPaginationSearchRESTModel searchRestModel = new EmissionPaginationSearchRESTModel();
		searchRestModel.setPage(0);
		searchRestModel.setEntriesPerPage(2);
		searchRestModel.setSortBy(EmissionSortFieldRESTModel.YEAR);
		searchRestModel.setSortDescending(false);
		searchRestModel.setFilterCountry(Country.GERMANY);
		
		try (Response response = httpPostMethod("/emissions/searchEmissionEntry", searchRestModel, null)) {
			
			assertEquals(200, response.getStatus());
			
			EmissionPaginationResultRESTModel result = response.readEntity(EmissionPaginationResultRESTModel.class);
			assertEquals(2, result.getTotalHits());
			
			List<EmissionEntryRESTModel> emissionEntrys = result.getEmissionEntrys();
			assertEquals(2, emissionEntrys.size());
			
			EmissionEntryRESTModel entryGermany2025 = emissionEntrys.get(0);
			assertEquals(emissionEntryGermany2025.getCountry(), entryGermany2025.getCountry());
			assertEquals(emissionEntryGermany2025.getYear(), entryGermany2025.getYear());
			
			EmissionEntryRESTModel entryGermany2026 = emissionEntrys.get(1);
			assertEquals(emissionEntryGermany2026.getCountry(), entryGermany2026.getCountry());
			assertEquals(emissionEntryGermany2026.getYear(), entryGermany2026.getYear());
		}
	}
}
