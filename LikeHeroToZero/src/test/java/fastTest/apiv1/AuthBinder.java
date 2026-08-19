package fastTest.apiv1;

import org.glassfish.jersey.internal.inject.AbstractBinder;

import dao.CredentialStore;
import dao.JpaCredentialDAO;
import jakarta.enterprise.context.RequestScoped;
import security.PasswordService;
import security.TokenService;

public class AuthBinder extends AbstractBinder {

	private final String issuer;
	
	public AuthBinder(String issuer) {
		this.issuer = issuer;
	}
	
	@Override
	protected void configure() {
		
		bind(TokenService.fromEnvironment(issuer)).to(TokenService.class);
		bind(new PasswordService()).to(PasswordService.class);
		bind(JpaCredentialDAO.class).to(CredentialStore.class).in(RequestScoped.class);
	}
}
