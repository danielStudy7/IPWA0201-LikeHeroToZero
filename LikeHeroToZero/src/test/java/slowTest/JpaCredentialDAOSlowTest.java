package slowTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import dao.CredentialStore.StoredCredentials;
import dao.JpaCredentialDAO;
import dao.UserDAO;
import model.User;

public class JpaCredentialDAOSlowTest extends AbstractSlowTestVorlage {

	private JpaCredentialDAO daoUnderTest; 
	private User user;
	
	@Override
	public void createTestData() {
		
		daoUnderTest = new JpaCredentialDAO(getEntityManager(), new UserDAO(getEntityManager()));
		
		user = new User("hirt7", "pw1234");
		getEntityManager().persist(user);
	}
	
	@Test
	public void testFindByLogin() throws Exception {
		
		Optional<StoredCredentials> result = daoUnderTest.findByLogin("hirt7");
		assertTrue(result.isPresent());
		assertEquals(result.get().userId(), user.getId());
		assertEquals(result.get().login(), user.getUserName());
		assertEquals(result.get().passwordHash(), user.getPassword());
	}

	@Test
	public void testFindByLogin_FindNothing_ReturnsEmpty() throws Exception {
		
		Optional<StoredCredentials> result = daoUnderTest.findByLogin("hirt");
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void testFindByLogin_nullValue_ReturnsEmpty() throws Exception {
		
		Optional<StoredCredentials> result = daoUnderTest.findByLogin(null);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void testFindByblankValue_ReturnsEmpty() throws Exception {
		
		Optional<StoredCredentials> result = daoUnderTest.findByLogin("");
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void testUpdatePasswordHash() throws Exception {
		
		daoUnderTest.updatePasswordHash(user.getId(), "999");
		
		User reloadedUser = daoUnderTest.getEntity(user.getId(), User.class);
		assertEquals(reloadedUser.getPassword(), "999");
	}
}
