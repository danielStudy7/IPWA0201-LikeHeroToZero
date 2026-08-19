package application.apiv1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;

public class LoginRESTModel {
	
	@NotBlank
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String login;
	
	@NotBlank
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String password;
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
