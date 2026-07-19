package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.UserSessionController;
import model.User;

class UserSessionControllerFastTest {

	private static UserSessionController controllerUnderTest;
	private static User user;
	
	@BeforeAll
	public static void setUp() {
		controllerUnderTest = new UserSessionController();
		user = new User("hirt", "test123");
	}
	
	@Test
	public void testCurrentUser() throws Exception {
		controllerUnderTest.setCurrentUser(user);
		
		User result = controllerUnderTest.getCurrentUser();
		
		assertEquals(user, result);
		
	}
	
	@Test
	public void testIsLoggedIn_LoggedIn() throws Exception {
		controllerUnderTest.setLoggedIn(true);
		
		boolean result = controllerUnderTest.isLoggedIn();
		
		assertTrue(result);
	}
	
	@Test
	public void testIsLoggedIn_IsNotLoggedIn() throws Exception {
		controllerUnderTest.setLoggedIn(false);
		
		boolean result = controllerUnderTest.isLoggedIn();
		
		assertFalse(result);
	}
	
	@Test
	public void testIsLoggedIn_Default() throws Exception {
		UserSessionController newController = new UserSessionController();
		
		boolean result = newController.isLoggedIn();
		
		assertFalse(result);
	}
}
