package fastTest;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.easymock.EasyMockExtension;

import dao.UserDAO;
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
}
