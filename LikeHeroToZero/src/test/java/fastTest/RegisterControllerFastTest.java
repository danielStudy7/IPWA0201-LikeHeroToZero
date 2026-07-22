package fastTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;

import static org.easymock.EasyMock.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import controller.RegisterController;
import controller.UserSessionController;
import dao.UserDAO;
import jakarta.faces.component.UIInput;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import model.User;

@ExtendWith(EasyMockExtension.class)
class RegisterControllerFastTest {
	
	@TestSubject
	private RegisterController controllerUnderTest = new RegisterController();
	
	@Mock
	private UserDAO userDao;
	@Mock
	private UserSessionController userSession;
	
	private User user;
	
	@BeforeEach
	public void setUp() throws Exception{
		user = new User();
		user.setId(100);
		user.setName("Daniel");
		user.setFamilyName("Hirt");
		user.setPassword("Test123");
		user.setUserName("dhirt");
	}
	
	@Test
	public void testSignUp_Success() throws Exception {
		controllerUnderTest.setSignUpUser(user);
		
		reset(userDao, userSession);
		userDao.createUser(user);
		expectLastCall();
		userSession.setCurrentUser(user);
		expectLastCall();
		userSession.setLoggedIn(true);
		expectLastCall();
		replay(userDao, userSession);
		
		String result = controllerUnderTest.signUp();
		
		assertEquals("backend.xhtml", result);
		verify(userDao, userSession);
	}
	
	@Test
	public void testSignUp_FailUserName() {
		user.setUserName("");
		
		String result = controllerUnderTest.signUp();
		
		assertEquals("login.xhtml", result);
	}
	
	@Test
	public void testSignUp_FailPassword() {
		user.setPassword("");
		
		String result = controllerUnderTest.signUp();
		
		assertEquals("login.xhtml", result);
	}
	
	@Test
	public void testValidateUserName_Success() throws Exception {
		ComponentSystemEvent event = createMock("event", ComponentSystemEvent.class);
	    UIInput uiInput = createMock("uiInput", UIInput.class);

	    reset(userDao, userSession);
	    expect(event.getComponent()).andReturn(uiInput);
	    expect(uiInput.getValue()).andReturn("nochNichtVergeben");
	    expect(userDao.getUserList()).andReturn(Arrays.asList(user));
	    replay(userDao, userSession, event, uiInput);

	    controllerUnderTest.postValidateUserName(event);   // tempUserName wird gesetzt

	    assertDoesNotThrow(() ->
	        controllerUnderTest.validateUserName(null, null, null));

	    verify(userDao, event, uiInput);
	}
	
	@Test
	public void testValidateUserName_Fail() throws Exception {
		ComponentSystemEvent event = createMock("event", ComponentSystemEvent.class);
	    UIInput uiInput = createMock("uiInput", UIInput.class);

	    reset(userDao, userSession);
	    expect(event.getComponent()).andReturn(uiInput);
	    expect(uiInput.getValue()).andReturn("dhirt");
	    expect(userDao.getUserList()).andReturn(Arrays.asList(user));
	    replay(userDao, userSession, event, uiInput);

	    controllerUnderTest.postValidateUserName(event);  

	    assertThrows(ValidatorException.class, () ->
	        controllerUnderTest.validateUserName(null, null, null));

	    verify(userDao, event, uiInput);
	}

}
