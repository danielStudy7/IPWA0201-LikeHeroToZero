package application.apiv1;

import java.util.Set;

import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class RESTApiApplication extends ResourceConfig {
	
	public RESTApiApplication() {
		packages("application.apiv1");
		
		OpenAPI openAPI = new OpenAPI()
				.info(new Info()
						.title(APIStrings.OAS_TITEL)
						.version(APIStrings.OAS_VERSION))
				.addServersItem(new Server().url(APIStrings.OAS_LOCALSERVER));
		
		SwaggerConfiguration swaggerConfig = new SwaggerConfiguration()
				.openAPI(openAPI)
				.prettyPrint(true)
				.resourcePackages(Set.of("application.apiv1"));
		
		register(new OpenApiResource().openApiConfiguration(swaggerConfig));
	}
}
