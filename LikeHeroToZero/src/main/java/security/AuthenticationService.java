package security;

import java.util.Optional;

import application.apiv1.model.TokenRESTModel;
import dao.CredentialStore;
import dao.CredentialStore.StoredCredentials;
import dao.JpaCredentialDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Named
@ApplicationScoped
public class AuthenticationService {
	
	private final CredentialStore credentialStore;
	private final PasswordService passwordService;
	private final TokenService tokenService;

	protected AuthenticationService() {
		this(null, null, null);
		// nur wegen CDI
	}
	
	@Inject
	public AuthenticationService(JpaCredentialDAO credentialStore, PasswordService passwordService, TokenService tokenService) {
		
		this.credentialStore = credentialStore;
		this.passwordService = passwordService;
		this.tokenService = tokenService;
	}
	
	public TokenRESTModel login(String userName, String password) {
		
		Optional<StoredCredentials> login = credentialStore.findByLogin(userName);
		
		if (login.isEmpty()) {
			passwordService.performDummyCheck();
			throw unauthorized();
		}
		
		CredentialStore.StoredCredentials credentials = login.get();
		if(!passwordService.matches(password, credentials.passwordHash())) {
			throw unauthorized();
		}
		
		TokenRESTModel response = new TokenRESTModel();
		response.setAccessToken(tokenService.createToken(credentials.userId(), credentials.login()));
		response.setExpiresInSeconds(tokenService.getTimeToLive().toSeconds());
		
		return response;
				
	}

	private static WebApplicationException unauthorized() {
		
		return new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"api\"")
				.build());
	}
}
