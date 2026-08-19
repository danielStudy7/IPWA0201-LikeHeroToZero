package security;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class TokenService {

	public static final String CLAIM_USER_ID = "uid";
	
	public static final String ENV_SECRET = "JWT_SECRET";
	public static final String ENV_TTL_MINUTES = "JWT_TTL_MINUTES";
	public static final String DEFAULT_TTL_MINUTES = "60";
	public static final int MIN_SECRET_LENGTH = 32;
	public static final long CLOCK_SKEW_SECONDS = 5L;
	
	private final String issuer;
	private final Algorithm algorithm;
	private final JWTVerifier verifier;
	private final Duration timeToLive;

	public TokenService(String issuer, String secret, Duration timeToLive) {
		
		if (issuer == null || issuer.isBlank()) {
			throw new IllegalArgumentException("Issuer darf nicht null sein");
		}
		
		if (secret == null || secret.length() < MIN_SECRET_LENGTH) {
			throw new IllegalArgumentException("JWT Secret muss min. " + MIN_SECRET_LENGTH + " Zeichen lang sein.");
		}
		
		if (timeToLive == null || timeToLive.isNegative() || timeToLive.isZero()) {
			throw new IllegalArgumentException("Token expired.");
		}
		
		this.issuer = issuer;
		this.algorithm = Algorithm.HMAC256(secret);
		this.verifier = JWT.require(algorithm)
				.withIssuer(issuer)
				.acceptLeeway(CLOCK_SKEW_SECONDS)
				.build();
		this.timeToLive = timeToLive;
	}
	
	public static TokenService fromEnvironment(String issuer) {
		
		String secret = System.getenv(ENV_SECRET);
		if (secret == null || secret.isBlank()) {
			throw new IllegalArgumentException("Environment variable " + ENV_SECRET + " nicht gesetzt.");
		}
		
		long minutes = Long.parseLong(Optional.ofNullable(System.getenv(ENV_TTL_MINUTES)).orElse(DEFAULT_TTL_MINUTES));
		
		return new TokenService(issuer, secret, Duration.ofMinutes(minutes));
	}
	
	public String createToken(UUID userId, String login) {
		
		Instant now = Instant.now();
		return JWT.create()
				.withIssuer(issuer)
				.withSubject(login)
				.withClaim(CLAIM_USER_ID, userId.toString())
				.withIssuedAt(now)
				.withExpiresAt(now.plus(timeToLive))
				.sign(algorithm);
	}
	
	public DecodedJWT verify(String token) {
		
		return verifier.verify(token);
	}
	
	public Duration getTimeToLive() {
		
		return timeToLive;
	}
}
