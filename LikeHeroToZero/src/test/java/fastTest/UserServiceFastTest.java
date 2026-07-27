package fastTest;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.easymock.EasyMockExtension;

import dao.UserDAO;
import jakarta.persistence.NoResultException;
import model.User;
import service.UserService;

@ExtendWith(EasyMockExtension.class)
public class UserServiceFastTest {

	@TestSubject
	private UserService serviceUnderTest = new UserService();
	
	@Mock
	private UserDAO userDao;
	
	@Test
	public void testIsLoginValid_Success() throws Exception {
		String username = "admin";
		String password = "secure";
		User adminUser = new User("admin", "secure");
		
		reset(userDao);
		expect(userDao.getEntityList(User.class)).andReturn(Arrays.asList(adminUser));
		replay(userDao);
		
		assertTrue(serviceUnderTest.isLoginValid(username, password));
		
		verify(userDao);
	}
	
	@Test
	public void testIsLoginValid_FailWrongPassword() throws Exception {
		String username = "admin";
		String password = "secure1";
		User adminUser = new User("admin", "secure");
		
		reset(userDao);
		expect(userDao.getEntityList(User.class)).andReturn(Arrays.asList(adminUser));
		replay(userDao);
		
		assertFalse(serviceUnderTest.isLoginValid(username, password));
		
		verify(userDao);
	}
	
	@Test
	public void testIsLoginValid_FailWrongUsername() throws Exception {
		String username = "admin1";
		String password = "secure";
		User adminUser = new User("admin", "secure");
		
		reset(userDao);
		expect(userDao.getEntityList(User.class)).andReturn(Arrays.asList(adminUser));
		replay(userDao);
		
		assertFalse(serviceUnderTest.isLoginValid(username, password));
		
		verify(userDao);
	}
	
	@Test
	public void testIsUsernameValid_Success() throws Exception {
		String username = "admin1";
		User adminUser = new User("admin", "secure");
		
		reset(userDao);
		expect(userDao.getEntityList(User.class)).andReturn(Arrays.asList(adminUser));
		replay(userDao);
		
		assertTrue(serviceUnderTest.isUsernameValid(username));
		
		verify(userDao);
	}
	
	@Test
	public void testIsUsernameValid_SuccessFirstUser() throws Exception {
		String username = "admin1";
		
		reset(userDao);
		expect(userDao.getEntityList(User.class)).andReturn(Collections.emptyList());
		replay(userDao);
		
		assertTrue(serviceUnderTest.isUsernameValid(username));
		
		verify(userDao);
	}
	
	@Test
	public void testIsUsernameValid_Fail() throws Exception {
		String username = "admin";
		User adminUser = new User("admin", "secure");
		
		reset(userDao);
		expect(userDao.getEntityList(User.class)).andReturn(Arrays.asList(adminUser));
		replay(userDao);
		
		assertFalse(serviceUnderTest.isUsernameValid(username));
		
		verify(userDao);
	}
	
	@Test
	void testCreateUser() throws Exception {
		User newUser = new User("hirt", "pw");
		
		reset(userDao);
		userDao.createEntity(newUser);
		expectLastCall();
		replay(userDao);
		
		serviceUnderTest.createUser(newUser);
		
		verify(userDao);
	}
	
	@Test
	void testUpdateUser() throws Exception {
		User newUser = new User("hirt", "pw");
		
		reset(userDao);
		userDao.updateEntity(newUser);
		expectLastCall();
		replay(userDao);
		
		serviceUnderTest.updateUser(newUser);
		
		verify(userDao);
	}
	
	@Test
	void testGetUserByUsername() throws Exception {
		User user = new User("hirt", "pw");
		
		reset(userDao);
		expect(userDao.getUserByUsername(user.getUserName())).andReturn(user);
		replay(userDao);
		
		User result = serviceUnderTest.getUserByUsername(user.getUserName());
		
		assertEquals(user.getUserName(), result.getUserName());
		
		verify(userDao);
	}
	
	@Test
	void testGetUserByUsername_NullResult() throws Exception {

		reset(userDao);
		expect(userDao.getUserByUsername("notAvailable")).andReturn(null);
		replay(userDao);
		
		User result = serviceUnderTest.getUserByUsername("notAvailable");
		
		assertNull(result);
		
		verify(userDao);
	}
	
	@Test
	void testGetUserByUsername_CatchException() throws Exception {

		reset(userDao);
		userDao.getUserByUsername("notAvailable");
		expectLastCall().andThrow(new NoResultException());
		replay(userDao);
		
		User result = serviceUnderTest.getUserByUsername("notAvailable");
		
		assertNull(result);
		
		verify(userDao);
	}
}
