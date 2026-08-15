package application.apiv1.operations;

import java.util.List;
import java.util.stream.Collectors;

import application.apiv1.mapper.EmissionEntryToRESTModel;
import application.apiv1.model.EmissionRESTModel;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.EmissionEntry;
import service.EmissionEntryService;

@Path("/emissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmissionOperations {
	
	@Inject
	private EmissionEntryService emissionEntryService;
	
	@GET
	@Path("/getAllEmissions")
	public List<EmissionRESTModel> getAllEmissionEntrys() {
		List<EmissionEntry> emissionEntrys = emissionEntryService.findAll();
		
		return emissionEntrys.stream().map(entry -> EmissionEntryToRESTModel.mapToRESTModel(entry)).collect(Collectors.toList());
	}
}
