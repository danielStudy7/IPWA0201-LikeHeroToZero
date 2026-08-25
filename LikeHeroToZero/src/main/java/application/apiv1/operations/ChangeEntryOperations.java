package application.apiv1.operations;

import java.util.List;

import application.apiv1.mapper.ChangeEntryCreateRESTModelToChangeEntry;
import application.apiv1.mapper.ChangeEntryToRESTModel;
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
import model.ChangeEntry;
import model.ChangeEntryCreateRESTModel;
import model.ChangeEntryRESTModel;
import model.UUIDListRESTModel;
import security.Secured;
import security.UserPrincipal;
import service.ChangeEntryService;

@Tag(name = "Änderungseinträge")
@Path("/changeEntry")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChangeEntryOperations {

	@Inject
	private ChangeEntryService changeEntryService;
	
	@POST
	@Path("/accept")
	@Operation(summary = "Akzeptiert einen Änderungseintrag.", description = "Benötigt eine Authentifizierung.")
	@Secured
	public void acceptChangeEntry(@Context SecurityContext securityContext, UUIDListRESTModel model) throws FailedOperationException {
		List<String> uuids = model.getUuids();
		
		for (String uuid : uuids) {
			ChangeEntry changeEntry = changeEntryService.getChangeEntry(uuid);
			changeEntryService.acceptChange(changeEntry);
		}
	}
	
	@POST
	@Path("/decline")
	@Operation(summary = "Lehnt einen Änderungseintrag ab.", description = "Benötigt eine Authentifizierung.")
	@Secured
	public void declineChangeEntry(@Context SecurityContext securityContext, UUIDListRESTModel model) throws FailedOperationException {
		
		List<String> uuids = model.getUuids();
		
		for (String uuid : uuids) {
			ChangeEntry changeEntry = changeEntryService.getChangeEntry(uuid);
			changeEntryService.declineChange(changeEntry);
		}
	}
	
	@GET
	@Path("/list")
	@Operation(summary = "Gibt alle Änderungseinträge aus.", description = "Benötigt eine Authentifizierung.")
	@Secured
	public List<ChangeEntryRESTModel> listChangeEntrys() {
		
		List<ChangeEntry> changeEntrys = changeEntryService.listAll();

		ChangeEntryToRESTModel mapper = new ChangeEntryToRESTModel();
		
		return mapper.map(changeEntrys);
	}
	
	@POST
	@Path("/create")
	@Operation(summary = "Erstellt einen neuen Änderungseintrag.", description = "Benötigt eine Authentifizierung.")
	@Secured
	public ChangeEntryRESTModel createChangeEntrys(@Context SecurityContext securityContext, ChangeEntryCreateRESTModel model) throws FailedOperationException {

		ChangeEntryCreateRESTModelToChangeEntry requestMapper = new ChangeEntryCreateRESTModelToChangeEntry();
		ChangeEntry changeEntry = requestMapper.map(model);
		
		UserPrincipal principal = (UserPrincipal) securityContext.getUserPrincipal();
		ChangeEntry result = changeEntryService.createChangeEntry(changeEntry, model.getEmissionEntryUUID(), principal.id());
		
		ChangeEntryToRESTModel responseMapper = new ChangeEntryToRESTModel();
		
		return responseMapper.map(result);
	}
}
