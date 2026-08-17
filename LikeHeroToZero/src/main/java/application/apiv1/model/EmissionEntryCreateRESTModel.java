package application.apiv1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import model.Country;

public class EmissionEntryCreateRESTModel {
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private double emissions;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private int year;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private boolean checked;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private Country country;
	
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
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
}
