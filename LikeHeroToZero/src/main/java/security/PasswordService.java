package security;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class PasswordService {

	private static final int COST = 12;
	private static final String DUMMY_HASH = BCrypt.hashpw("dummy", BCrypt.gensalt(COST));
	
	public PasswordService() {
		//
	}
	
	public String hash(String plainPassword) {
		
		if (plainPassword == null || plainPassword.isEmpty()) {
			throw new IllegalArgumentException("Password muss ausgefüllt sein");
		}
		
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST));
	}
	
	public boolean matches(String plainPassword, String storedValue) {
		
		if (plainPassword == null || storedValue == null || storedValue.isEmpty()) {
			return false;
		}
		
		if (isBcryptHash(storedValue)) {
			return BCrypt.checkpw(plainPassword, storedValue);
		}
		
		return false;
	}
	
	public void performDummyCheck() {
		BCrypt.checkpw("dummy", DUMMY_HASH);
	}
	
	private static boolean isBcryptHash(String value) {
		
		return value.length() == 60 && (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
	}
}
