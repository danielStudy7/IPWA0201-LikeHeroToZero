package slowTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import dao.UserDAO;
import jakarta.persistence.NoResultException;
import model.User;

class UserDaoSlowTest extends AbstractSlowTestVorlage {

	private UserDAO daoUnderTest;
	private User userDaniel;
	private User userAdmin;
	
	@Override
	public void createTestData() {
		daoUnderTest = new UserDAO(getEntityManager());
		userDaniel = new User("daniel", "pw123");
		
		getEntityManager().persist(userDaniel);
		
		userAdmin = new User("admin", "sicher123");
		getEntityManager().persist(userAdmin);
		
		getTransaction().commit();
	}
	
	@Test
	public void testGetUserList() throws Exception {
		List<User> userList = daoUnderTest.getUserList();
		
		assertEquals(2, userList.size());
	}
	
	@Test
	public void testGetUser() throws Exception {
		User result = daoUnderTest.getUser("daniel");
		
		assertEquals(userDaniel, result);
	}
	
	@Test
	public void testCreateUser() throws Exception {
		User newUser = new User("hirt", "pw");
		newUser.setName("Daniel");
		newUser.setFamilyName("Hirt");
		
		daoUnderTest.createUser(newUser);
		User result = daoUnderTest.getUser("hirt");
		
		assertEquals(newUser, result);
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		userAdmin.setName("Daniel");
		userAdmin.setFamilyName("Hirt");
		
		daoUnderTest.updateUser(userAdmin);
		User result = daoUnderTest.getUser("admin");
		
		assertAll(
			() -> assertEquals("Daniel", result.getName()),
			() -> assertEquals("Hirt", result.getFamilyName()),
			() -> assertEquals("admin", result.getUserName()),
			() -> assertEquals("sicher123", result.getPassword()),
			() -> assertEquals(userAdmin.getId(), result.getId())
		);
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		daoUnderTest.deleteUser(userDaniel);
		
		try {
			daoUnderTest.getUser("daniel");
			fail("Hier sollte eine Exception fliegen, weil der User nicht mehr existiert.");
		}
		catch (NoResultException e) {
			assertEquals("No result found for query [<criteria>]", e.getMessage());
		}
	}
	
}
