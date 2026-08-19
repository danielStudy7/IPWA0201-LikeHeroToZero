package dao;

import java.util.Optional;
import java.util.UUID;

import common.FailedOperationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.User;

@Named
@ApplicationScoped
public class JpaCredentialDAO extends AbstractDAO implements CredentialStore{

	private UserDAO userDao;
	
	public JpaCredentialDAO()
	{
		super();
		this.userDao = new UserDAO();
	}
	
	public JpaCredentialDAO(EntityManager entityManager, UserDAO userDao) {
		super(entityManager);
		this.userDao = userDao;
	}
	
	@Override
	public Optional<StoredCredentials> findByLogin(String login) {
		
		if (login == null || login.isBlank()) {
			return Optional.empty();
		}
		
		try {
			User user = userDao.getUserByUsername(login);
			
			return Optional.of(new StoredCredentials(user.getId(), user.getUserName(), user.getPassword()));
		}
		catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public void updatePasswordHash(UUID userId, String newPasswordHash) throws FailedOperationException {
		
		User user = getEntity(userId, User.class);
		
		if (user == null) {
			return;
		}
		
		if (getTransaction().isActive()) {
			user.setPassword(newPasswordHash);
			updateEntity(user);
			return;
		}
		
		getTransaction().begin();
		try {
			user.setPassword(newPasswordHash);
			updateEntity(user);
		}
		catch (RuntimeException e) {
			if (getTransaction().isActive()) {
				getTransaction().rollback();
			}
			
			throw e;
		}
	}
}
