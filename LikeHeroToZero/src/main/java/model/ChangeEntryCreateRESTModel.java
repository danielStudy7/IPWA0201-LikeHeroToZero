package model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class ChangeEntryCreateRESTModel {
	
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private double emissions;
	private int year;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String infoText;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String source;
	private Country country;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String emissionEntryUUID;
	
	public double getEmissions() {
		return emissions;
	}
	
	public void setEmissions(double emissions) {
		this.emissions = emissions;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getInfoText() {
		return infoText;
	}
	
	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	public String getEmissionEntryUUID() {
		return emissionEntryUUID;
	}

	public void setEmissionEntryUUID(String emissionEntryUUID) {
		this.emissionEntryUUID = emissionEntryUUID;
	}
}
