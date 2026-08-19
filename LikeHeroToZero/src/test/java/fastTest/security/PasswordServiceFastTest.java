package fastTest.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.easymock.EasyMockExtension;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;

import security.PasswordService;

@ExtendWith(EasyMockExtension.class)
public class PasswordServiceFastTest {

	@TestSubject
	private PasswordService serviceUnderTest;
	
	@Test
	public void testHash() throws Exception {
		
		String dummyPw = "pW123!";
		
		String result = serviceUnderTest.hash(dummyPw);
		String result1 = serviceUnderTest.hash(dummyPw);
		
		assertNotEquals(result, result1);
	}
	
	@Test
	public void testHash_ThrowsOnEmptyValue() throws Exception {
		
		String dummyPw = "";
		
		try {
			serviceUnderTest.hash(dummyPw);
			fail("Hier wird eine Exception erwartet weil das Passwort empty ist");
		}
		catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Password muss ausgefüllt sein");
		}
	}
	
	@Test
	public void testHash_ThrowsOnNullValue() throws Exception {
		
		try {
			serviceUnderTest.hash(null);
			fail("Hier wird eine Exception erwartet weil das Passwort null ist");
		}
		catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "Password muss ausgefüllt sein");
		}
	}
	
	@Test
	public void testMatches_FailNotCrypted() throws Exception {
		
		String plainPassword = "pW123!";
		String storedValue = "321654987";
		
		assertFalse(serviceUnderTest.matches(plainPassword, storedValue));
	}
	
	@Test
	public void testMatches_FailNull() throws Exception {
		
		assertFalse(serviceUnderTest.matches(null, null));
	}
	
	@Test
	public void testMatches_Success() throws Exception {
		
		String plainPassword = "pW123!";
		String storedValue = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
		
		assertTrue(serviceUnderTest.matches(plainPassword, storedValue));
	}
	
	
}
