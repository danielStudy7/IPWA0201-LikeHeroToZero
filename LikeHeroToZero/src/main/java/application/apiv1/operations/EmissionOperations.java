package application.apiv1.operations;

import java.util.List;

import application.apiv1.mapper.EmissionEntryToRESTModel;
import application.apiv1.model.EmissionEntryRESTModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.EmissionEntry;
import service.EmissionEntryService;

@Tag(name = "Emissionen")
@Path("/emissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmissionOperations {
	
	@Inject
	private EmissionEntryService emissionEntryService;
	
	@GET
	@Path("/getAllEmissions")
	@Operation(summary = "Liefert alle vorhandenen Emissionseinträge zurück.")
	public List<EmissionEntryRESTModel> getAllEmissionEntrys() {
		
		EmissionEntryToRESTModel responseMapper = new EmissionEntryToRESTModel();
		
		List<EmissionEntry> emissionEntrys = emissionEntryService.findAll();
		
		return responseMapper.map(emissionEntrys);
	}
}
