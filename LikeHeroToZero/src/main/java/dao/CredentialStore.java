package dao;

import java.util.Optional;
import java.util.UUID;

import common.FailedOperationException;

public interface CredentialStore {

	Optional<StoredCredentials> findByLogin(String login);
	
	void updatePasswordHash(UUID userId, String newPasswordHash) throws FailedOperationException;
	
	record StoredCredentials(UUID userId, String login, String passwordHash) {
		
	}
}
