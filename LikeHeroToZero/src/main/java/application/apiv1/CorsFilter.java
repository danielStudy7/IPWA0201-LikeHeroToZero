package application.apiv1;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {

// Prüfen woher die Anfragen kommen und nur die Whitelist zulassen
//	private static final Set<String> ALLOWED = Set.of(
//	        "https://editor.swagger.io",
//	        "http://localhost:8080");
	
	@Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) {
        String origin = req.getHeaderString("Origin");
        if (origin != null) {
            res.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
            res.getHeaders().putSingle("Vary", "Origin");
        }
        res.getHeaders().putSingle("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        res.getHeaders().putSingle("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        res.getHeaders().putSingle("Access-Control-Max-Age", "3600");
    }
}
