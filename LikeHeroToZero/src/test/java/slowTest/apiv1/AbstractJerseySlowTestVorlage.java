package slowTest.apiv1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.time.Duration;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.Binder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import application.apiv1.AuthenticationFilter;
import application.apiv1.CorsFilter;
import application.apiv1.CorsPreflightFilter;
import application.apiv1.model.LoginRESTModel;
import application.apiv1.model.TokenRESTModel;
import dao.JpaCredentialDAO;
import dao.UserDAO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;
import security.AuthenticationService;
import security.PasswordService;
import security.TokenService;
import slowTest.AbstractSlowTestVorlage;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class AbstractJerseySlowTestVorlage extends AbstractSlowTestVorlage{
	
	private static final String DEFAULT_RESOURCE_PACKAGE = "application.apiv1";
	private static final String JWT_TEST_SECRET = "12345678901234567890123456789123222";
	
	private URI baseUri;
	private HttpServer server;
	private Client client; 
	
	private TokenService tokenService = new TokenService("test", JWT_TEST_SECRET, Duration.ofHours(1));
	
	protected abstract Binder createTestBinder();
	
	protected Binder createAuthBinder() {
	
		return new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(tokenService).to(TokenService.class);
				bind(new PasswordService()).to(PasswordService.class);
				
				bindFactory(() -> new AuthenticationService(new JpaCredentialDAO(getEntityManager(), new UserDAO(getEntityManager())), new PasswordService(), tokenService)).to(AuthenticationService.class);
			}
		};
	}
	
	protected String[] getResourcePackages() {
		
		return new String[] { DEFAULT_RESOURCE_PACKAGE};
	}
	
	@BeforeAll
	protected void startGrizzly() {
		
		baseUri = URI.create("http://localhost:" + findFreePort() + "/hirt");
		
		ResourceConfig config = new ResourceConfig()//
				.packages(getResourcePackages())
				.register(createTestBinder())
				.register(createAuthBinder())
				.register(AuthenticationFilter.class)
				.register(CorsFilter.class)
				.register(CorsPreflightFilter.class);
		
		server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
		client = ClientBuilder.newClient();
	}
	
	@AfterAll
	protected void stopGrizzly() {
		
		if (client != null) {
			client.close();
		}
		
		if (server != null) {
			server.shutdown();
		}
	}
	
	protected WebTarget target(String path) {
		
		return client.target(baseUri).path(path);
	}
	
	protected URI getBaseUri() {
		return baseUri;
	}
	
	private static int findFreePort() {
		
		try (ServerSocket socket = new ServerSocket(0)) {
			return socket.getLocalPort();
		}
		catch (IOException e) {
			throw new IllegalStateException("Kein freier Port ist verfügbar.", e);
		}
	}
	
	public Response httpGetMethod(String path) {
		
		return target(path)
				.request(MediaType.APPLICATION_JSON)
				.get();
	}
	
	public Response httpGetMethod(String path, String token) {
		return target(path)
				.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.get();
	}
	
	public Response httpGetMethodWithParam(String path, String param, Object value) {
		
		return target(path)
				.resolveTemplate(param, value)
				.request(MediaType.APPLICATION_JSON)
				.get();
	}
	
	public Response httpDeleteMethodWithParam(String path, String param, Object value) {
		
		return target(path)
				.resolveTemplate(param, value)
				.request(MediaType.APPLICATION_JSON)
				.delete();
	}
	
	public Response httpPostMethod(String path, Object restModel, String token) {
		
		Entity<Object> entity = createEntityFromRESTModel(restModel);
		
		return target(path)
				.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.post(entity);
	}
	
	public String loginAndGetToken(String userName, String password) {
		
		LoginRESTModel model = createLoginRESTModel(userName, password);
		Entity<Object> entity = createEntityFromRESTModel(model);
		
		Response response = target("/auth/login")
								.request(MediaType.APPLICATION_JSON)
								.post(entity);
		
		if (response.getStatus() != 200) {
			throw new IllegalStateException("Login ist fehlgeschlagen" + "\nStatus " + response.getStatus() + " " + response.getStatusInfo() + "\n" + response.readEntity(String.class));
		}
		
		TokenRESTModel tokenModel = response.readEntity(TokenRESTModel.class);
		
		return tokenModel.getAccessToken();
	}

	private Entity<Object> createEntityFromRESTModel(Object model) {
		
		return Entity.entity(model, MediaType.APPLICATION_JSON);
	}

	private LoginRESTModel createLoginRESTModel(String userName, String password) {
		
		LoginRESTModel model = new LoginRESTModel();
		model.setLogin(userName);
		model.setPassword(password);
		return model;
	}
	
	public String createToken(User user) {
		return tokenService.createToken(user.getId(), user.getUserName());
	}
}
