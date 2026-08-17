package slowTest.apiv1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.internal.inject.Binder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import slowTest.AbstractSlowTestVorlage;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class AbstractJerseySlowTestVorlage extends AbstractSlowTestVorlage{
	
	private static final String DEFAULT_RESOURCE_PACKAGE = "application.apiv1.operations";
	
	private URI baseUri;
	private HttpServer server;
	private Client client; 
	
	protected abstract Binder createTestBinder();
	
	protected String[] getResourcePackages() {
		
		return new String[] { DEFAULT_RESOURCE_PACKAGE};
	}
	
	@BeforeAll
	protected void startGrizzly() {
		
		baseUri = URI.create("http://localhost:" + findFreePort() + "/hirt");
		
		ResourceConfig config = new ResourceConfig()//
				.packages(getResourcePackages())
				.register(createTestBinder());
		
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
	
	public Response httpPostMethod(String path, Object restModel) {
		
		Entity<Object> entity = Entity.entity(restModel, MediaType.APPLICATION_JSON);
		
		return target(path)
				.request(MediaType.APPLICATION_JSON)
				.post(entity);
	}
}
