package model;


public class ChangeEntryRESTModel {
	
	private String uuid;
	private boolean accepted;
	private boolean declined;
	private double emissions;
	private int year;
	private String infoText;
	private String source;
	private Country country;
	private String emissionEntryUUID;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public boolean isAccepted() {
		return accepted;
	}
	
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
	public boolean isDeclined() {
		return declined;
	}
	
	public void setDeclined(boolean declined) {
		this.declined = declined;
	}
	
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
