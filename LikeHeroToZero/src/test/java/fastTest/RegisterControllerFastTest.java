package fastTest;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
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
import service.UserService;

@ExtendWith(EasyMockExtension.class)
class RegisterControllerFastTest {
	
	@TestSubject
	private RegisterController controllerUnderTest = new RegisterController();
	
	@Mock
	private UserDAO userDao;
	@Mock
	private UserSessionController userSession;
	@Mock
	private UserService userService;
	
	private User user;
	
	@BeforeEach
	public void setUp() throws Exception{
		user = new User();
		user.setId(UUID.randomUUID());
		user.setName("Daniel");
		user.setFamilyName("Hirt");
		user.setPassword("Test123");
		user.setUserName("dhirt");
	}
	
	@Test
	public void testSignUp_Success() throws Exception {
		controllerUnderTest.setSignUpUser(user);
		
		reset(userSession);
		userSession.setCurrentUser(user);
		expectLastCall();
		userSession.setLoggedIn(true);
		expectLastCall();
		replay(userSession);
		
		String result = controllerUnderTest.signUp();
		
		assertEquals("backend.xhtml", result);
		verify(userSession);
	}
	
	@Test
	public void testSignUp_FailUserName() throws Exception {
		user.setUserName("");
		
		String result = controllerUnderTest.signUp();
		
		assertEquals("login.xhtml", result);
	}
	
	@Test
	public void testSignUp_FailPassword() throws Exception  {
		user.setPassword("");
		
		String result = controllerUnderTest.signUp();
		
		assertEquals("login.xhtml", result);
	}
	
	@Test
	public void testValidateUserName_Success() throws Exception {
		ComponentSystemEvent event = createMock("event", ComponentSystemEvent.class);
	    UIInput uiInput = createMock("uiInput", UIInput.class);

	    reset(userSession, userService, event, uiInput);
	    expect(event.getComponent()).andReturn(uiInput);
	    expect(uiInput.getValue()).andReturn("nochNichtVergeben");
	    expect(userService.isUsernameValid("nochNichtVergeben")).andReturn(true);
	    replay(userService, userSession, event, uiInput);

	    controllerUnderTest.postValidateUserName(event);   // tempUserName wird gesetzt

	    assertDoesNotThrow(() ->
	        controllerUnderTest.validateUserName(null, null, null));

	    verify(userSession, userService, event, uiInput);
	}
	
	@Test
	public void testValidateUserName_Fail() throws Exception {
		ComponentSystemEvent event = createMock("event", ComponentSystemEvent.class);
	    UIInput uiInput = createMock("uiInput", UIInput.class);

	    reset(userService, userSession, event, uiInput);
	    expect(event.getComponent()).andReturn(uiInput);
	    expect(uiInput.getValue()).andReturn("dhirt");
	    expect(userService.isUsernameValid("dhirt")).andReturn(false);
	    replay(userService, userSession, event, uiInput);

	    controllerUnderTest.postValidateUserName(event);  

	    assertThrows(ValidatorException.class, () ->
	        controllerUnderTest.validateUserName(null, null, null));

	    verify(userSession, userService, event, uiInput);
	}

}
