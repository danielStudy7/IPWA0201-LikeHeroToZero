package application.apiv1.model;

public enum EmissionSortFieldRESTModel {
	
	YEAR("year"),
	COUNTRY("country"),
	EMISSIONS("emissions");
	
	private String dbName;
	
	EmissionSortFieldRESTModel(String dbName) {
		this.dbName = dbName;
	}
	
	public String getDbName() {
		return this.dbName;
	}
}
