package application.apiv1.operations;

import java.util.List;

import application.apiv1.mapper.EmissionEntryCreateRESTModelToEmissionEntry;
import application.apiv1.mapper.EmissionEntryToRESTModel;
import application.apiv1.model.EmissionEntryCreateRESTModel;
import application.apiv1.model.EmissionEntryRESTModel;
import common.FailedOperationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import model.EmissionEntry;
import security.Secured;
import security.UserPrincipal;
import service.EmissionEntryService;

@Tag(name = "Emissionen")
@Path("/emissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
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
	
	@POST
	@Path("/createEmissionEntry")
	@Operation(summary = "Erstellen eines neuen Emission-Eintrags.")
	public EmissionEntryRESTModel createEmissionEntry(@Context SecurityContext securityContext, EmissionEntryCreateRESTModel model) throws FailedOperationException {
		
		EmissionEntryCreateRESTModelToEmissionEntry requestMapper = new EmissionEntryCreateRESTModelToEmissionEntry();
		EmissionEntryToRESTModel responseMapper = new EmissionEntryToRESTModel();
		
		UserPrincipal principal = (UserPrincipal) securityContext.getUserPrincipal();
		EmissionEntry emissionEntry = emissionEntryService.createAndReturnEmissionEntry(requestMapper.map(model), principal.id());
		
		return responseMapper.map(emissionEntry);
	}
}
