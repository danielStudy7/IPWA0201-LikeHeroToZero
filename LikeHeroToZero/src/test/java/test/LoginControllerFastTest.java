package test;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import controller.LoginController;
import controller.UserSessionController;
import dao.UserDAO;
import jakarta.faces.component.UIInput;
import jakarta.faces.event.ComponentSystemEvent;
import model.User;

@ExtendWith(EasyMockExtension.class)
class LoginControllerFastTest {
	@TestSubject
	private LoginController controllerUnderTest;
	
	@Mock
	private UserDAO userDao;
	@Mock
	private UserSessionController userSessionController;
	
	private User loginUser;
	
	@BeforeEach
	public void setUp() throws Exception {
		loginUser = new User();
		loginUser.setId(100);
		loginUser.setName("Daniel");
		loginUser.setFamilyName("Hirt");
		loginUser.setPassword("geheim");
		loginUser.setUserName("dhirt");
	}

	@Test
	public void testLogin_Success() throws Exception {
		controllerUnderTest.setLoginUser(loginUser);
		
		reset(userDao, userSessionController);
		expect(userDao.getUser(loginUser.getUserName())).andReturn(loginUser);
		userSessionController.setCurrentUser(loginUser);
		expectLastCall();
		userSessionController.setLoggedIn(true);
		expectLastCall();
		replay(userDao, userSessionController);
		
		String result = controllerUnderTest.login();
		
		assertEquals("backend.xhtml", result);
		
		verify(userDao, userSessionController);
	}
	
	@Test
	public void testLogout_Success() throws Exception {
		reset(userDao, userSessionController);
		expect(userSessionController.getCurrentUser()).andReturn(loginUser);
		userSessionController.setCurrentUser(null);
		expectLastCall();
		userSessionController.setLoggedIn(false);
		expectLastCall();
		replay(userDao, userSessionController);
		
		String result = controllerUnderTest.logout();
		
		assertEquals("index.xhtml", result);
		
		verify(userDao, userSessionController);
	}
	
	@Test
	public void testLogout_Fail() throws Exception {
		reset(userDao, userSessionController);
		expect(userSessionController.getCurrentUser()).andReturn(null);
		replay(userDao, userSessionController);
		
		String result = controllerUnderTest.logout();
		
		assertEquals("index.xhtml", result);
		
		verify(userDao, userSessionController);
	}
	
	@Test
	public void testValidateLogin() throws Exception {
		String tempPassword = "geheim";
		controllerUnderTest.setLoginUser(loginUser);
		
		reset(userDao, userSessionController);
		expect(userDao.getUserList()).andReturn(Arrays.asList(loginUser));
		replay(userDao, userSessionController);
		
		assertDoesNotThrow(() -> controllerUnderTest.validateLogin(null, null, tempPassword));
		
		verify(userDao, userSessionController);
	}
	
	@Test
	public void testPostValidateUser() throws Exception {
		ComponentSystemEvent event = createMock("event", ComponentSystemEvent.class);
		UIInput uiInput = createMock("uiInput", UIInput.class);
		
		assertNotEquals(loginUser.getUserName(), controllerUnderTest.getLoginUser().getUserName());
		
		reset(userDao, userSessionController, event, uiInput);
		expect(event.getComponent()).andReturn(uiInput);
		expect(uiInput.getValue()).andReturn(loginUser.getUserName());
		replay(userDao, userSessionController, event, uiInput);
		
		controllerUnderTest.postValidateUser(event);
		
		assertEquals(loginUser.getUserName(), controllerUnderTest.getLoginUser().getUserName());
		
		verify(userDao, userSessionController, event, uiInput);
	}
}
