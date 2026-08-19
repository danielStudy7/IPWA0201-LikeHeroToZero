package security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@ApplicationScoped
public class AuthConfiguration {
	
    private static final String ISSUER = "hirt";
    
    @Produces
    @Singleton
    public TokenService createTokenService() {
        return TokenService.fromEnvironment(ISSUER);
    }
}
