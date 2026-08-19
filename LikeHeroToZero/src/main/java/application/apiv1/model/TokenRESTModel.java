package application.apiv1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class TokenRESTModel {

	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String accessToken;
	
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String tokenType = "Bearer";
	
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private long expiresInSeconds;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public long getExpiresInSeconds() {
		return expiresInSeconds;
	}

	public void setExpiresInSeconds(long expiresInSeconds) {
		this.expiresInSeconds = expiresInSeconds;
	}
}
