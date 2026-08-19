package security;

import java.security.Principal;
import java.util.UUID;

public record UserPrincipal(UUID id, String login) implements Principal {

	public UserPrincipal {
		
		if (id == null) {
			throw new IllegalArgumentException("ID muss gesetzt sein.");
		}
		
		if (login == null || login.isBlank()) {
			throw new IllegalArgumentException("Login muss gesetzt sein.");
		}
	}
	
	@Override
	public String getName() {
		return login;
	}
}
