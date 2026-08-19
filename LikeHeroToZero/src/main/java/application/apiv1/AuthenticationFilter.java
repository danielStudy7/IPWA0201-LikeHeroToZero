package application.apiv1;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import security.Secured;
import security.TokenService;
import security.UserPrincipal;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";
	private static final String CHALLENGE = "Bearer realm=\"api\"";
	
	private final TokenService tokenService;
	
	@Inject
	public AuthenticationFilter(TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String header = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		if (header == null || !header.startsWith(BEARER_PREFIX)) {
			abort(requestContext);
			return;
		}
		
		try {
			DecodedJWT jwt = tokenService.verify(header.substring(BEARER_PREFIX.length()).trim());
			UUID userId = UUID.fromString(jwt.getClaim(TokenService.CLAIM_USER_ID).asString());
			UserPrincipal principal = new UserPrincipal(userId, jwt.getSubject());
			boolean secure = requestContext.getSecurityContext().isSecure(); 
			requestContext.setSecurityContext(new TokenSecurityContext(principal, secure));
		}
		catch (JWTVerificationException | IllegalArgumentException | NullPointerException e) {
			abort(requestContext);
		}
	}
	
	private static void abort(ContainerRequestContext requestContext) {
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, CHALLENGE)
				.build());
	}
	
	private record TokenSecurityContext(UserPrincipal principal, boolean secure) implements SecurityContext {

		@Override
		public Principal getUserPrincipal() {
			return principal;
		}

		@Override
		public boolean isUserInRole(String role) {
			return false;
		}

		@Override
		public boolean isSecure() {
			return secure;
		}

		@Override
		public String getAuthenticationScheme() {
			return "Bearer";
		}
		
		
	}
}
