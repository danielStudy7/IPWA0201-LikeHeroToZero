package fastTest.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import security.TokenService;

@ExtendWith(EasyMockExtension.class)
public class TokenServiceFastTest {

    private static final String SECRET = "111111111111111111111111111111122";
    private static final String ISSUER = "issuer";
    private static final String LOGIN = "hirt7";
    private static final Duration TTL = Duration.ofMinutes(60);
 
    private TokenService createService() {
        return new TokenService(ISSUER, SECRET, TTL);
    }
 
    @Test
    public void testCreateTokenAndVerify() {
        TokenService serviceUnderTest = createService();
        UUID userId = UUID.randomUUID();
 
        assertTrue(serviceUnderTest.getTimeToLive().isPositive());
 
        String token = serviceUnderTest.createToken(userId, LOGIN);
 
        assertNotNull(token);
        assertEquals(3, token.split("\\.").length, "JWT besteht aus Header, Payload und Signatur");
 
        DecodedJWT verifyResult = serviceUnderTest.verify(token);
 
        assertEquals(ISSUER, verifyResult.getIssuer());
        assertEquals(LOGIN, verifyResult.getSubject());
        assertEquals(userId, UUID.fromString(verifyResult.getClaim(TokenService.CLAIM_USER_ID).asString()));
    }
 
    @Test
    public void testTimeToLiveIsAppliedToExpiry() {
        TokenService serviceUnderTest = createService();
 
        DecodedJWT verifyResult =
                serviceUnderTest.verify(serviceUnderTest.createToken(UUID.randomUUID(), LOGIN));
 
        Instant issuedAt = verifyResult.getIssuedAtAsInstant();
        Instant expiresAt = verifyResult.getExpiresAtAsInstant();
 
        assertNotNull(issuedAt);
        assertNotNull(expiresAt);
        assertEquals(TTL, Duration.between(issuedAt, expiresAt));
    }
 
    @Test
    public void testTokenIsVerifiableByAnotherInstanceWithSameSecret() {
        String token = createService().createToken(UUID.randomUUID(), LOGIN);
 
        assertEquals(LOGIN, new TokenService(ISSUER, SECRET, TTL).verify(token).getSubject());
    }
 
    @Test
    public void testConstructorRejectsBlankIssuer() {
        assertThrows(IllegalArgumentException.class, () -> new TokenService(null, SECRET, TTL));
        assertThrows(IllegalArgumentException.class, () -> new TokenService("", SECRET, TTL));
        assertThrows(IllegalArgumentException.class, () -> new TokenService("   ", SECRET, TTL));
    }
 
    @Test
    public void testConstructorRejectsWeakSecret() {
        assertThrows(IllegalArgumentException.class, () -> new TokenService(ISSUER, null, TTL));
        assertThrows(IllegalArgumentException.class,
                () -> new TokenService(ISSUER, "1".repeat(31), TTL));
    }
 
    @Test
    public void testConstructorAcceptsSecretAtMinimumLength() {
        assertNotNull(new TokenService(ISSUER, "1".repeat(32), TTL));
    }
 
    @Test
    public void testConstructorRejectsInvalidTimeToLive() {
        assertThrows(IllegalArgumentException.class, () -> new TokenService(ISSUER, SECRET, null));
        assertThrows(IllegalArgumentException.class,
                () -> new TokenService(ISSUER, SECRET, Duration.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new TokenService(ISSUER, SECRET, Duration.ofMinutes(-1)));
    }
 
    @Test
    public void testVerifyRejectsExpiredToken() {
        Instant past = Instant.now().minus(Duration.ofHours(2));
        String expiredToken = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(LOGIN)
                .withClaim(TokenService.CLAIM_USER_ID, UUID.randomUUID().toString())
                .withIssuedAt(past)
                .withExpiresAt(past.plus(Duration.ofMinutes(1)))
                .sign(Algorithm.HMAC256(SECRET));
 
        assertThrows(JWTVerificationException.class, () -> createService().verify(expiredToken));
    }
 
    @Test
    public void testVerifyRejectsForeignIssuer() {
        String foreignToken = JWT.create()
                .withIssuer("someone-else")
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plus(TTL))
                .sign(Algorithm.HMAC256(SECRET));
 
        assertThrows(JWTVerificationException.class, () -> createService().verify(foreignToken));
    }
 
    @Test
    public void testVerifyRejectsTokenSignedWithOtherSecret() {
        String otherSecret = "22222222222222222222222222222222";
        String foreignToken =
                new TokenService(ISSUER, otherSecret, TTL).createToken(UUID.randomUUID(), LOGIN);
 
        assertThrows(JWTVerificationException.class, () -> createService().verify(foreignToken));
    }
 
    @Test
    public void testVerifyRejectsTamperedPayload() {
        TokenService serviceUnderTest = createService();
        String token = serviceUnderTest.createToken(UUID.randomUUID(), LOGIN);
 
        String[] parts = token.split("\\.");
        String tamperedPayload = parts[1].substring(0, parts[1].length() - 1)
                + (parts[1].endsWith("A") ? "B" : "A");
        String tamperedToken = parts[0] + "." + tamperedPayload + "." + parts[2];
 
        assertThrows(JWTVerificationException.class, () -> serviceUnderTest.verify(tamperedToken));
    }
 
    @Test
    public void testVerifyRejectsMalformedToken() {
        TokenService serviceUnderTest = createService();
 
        assertThrows(JWTVerificationException.class, () -> serviceUnderTest.verify(""));
        assertThrows(JWTVerificationException.class, () -> serviceUnderTest.verify("not-a-token"));
        assertThrows(JWTVerificationException.class, () -> serviceUnderTest.verify("a.b.c"));
    }
 
    @Test
    public void testVerifyRejectsNullToken() {
        assertThrows(RuntimeException.class, () -> createService().verify(null));
    }
 
    @Test
    public void testVerifyAcceptsTokenWithoutUserIdClaim() {
        String tokenWithoutClaim = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(LOGIN)
                .withExpiresAt(Instant.now().plus(TTL))
                .sign(Algorithm.HMAC256(SECRET));
 
        DecodedJWT verifyResult = createService().verify(tokenWithoutClaim);
 
        assertNull(verifyResult.getClaim(TokenService.CLAIM_USER_ID).asString());
    }
}
