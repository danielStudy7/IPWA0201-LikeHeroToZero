package application.apiv1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class EmissionEntryRESTModel extends EmissionEntryCreateRESTModel {
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String id;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
