package application.apiv1.operations;


import application.apiv1.model.LoginRESTModel;
import application.apiv1.model.TokenRESTModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import security.AuthenticationService;
import security.Secured;
import security.UserPrincipal;

@Tag(name = "Authentifizierung")
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationOperations {

	private AuthenticationService authService;
	
	@Inject
	public AuthenticationOperations(AuthenticationService authService) {
		this.authService = authService;
	}
	
	@POST
	@Path("/login")
	@Operation(summary = "Login mit einem Benutzer.")
	public TokenRESTModel login(@NotNull @Valid LoginRESTModel model) {
		return authService.login(model.getLogin(), model.getPassword());
	}
	
	@GET
	@Path("/me")
	@Secured
	@Operation(summary = "Gibt den aktuellen Benutzer zurück.", description = "Benötigt eine Authentifizierung.")
	public UserPrincipal me(@Context SecurityContext securityContext) {
		return (UserPrincipal) securityContext.getUserPrincipal();
	}
}
