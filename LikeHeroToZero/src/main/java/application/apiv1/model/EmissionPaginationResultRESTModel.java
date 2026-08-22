package application.apiv1.model;

import java.util.List;

public class EmissionPaginationResultRESTModel {
	
	private int totalHits;
	private List<EmissionEntryRESTModel> emissionEntrys;
	
	public int getTotalHits() {
		return totalHits;
	}
	
	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}
	
	public List<EmissionEntryRESTModel> getEmissionEntrys() {
		return emissionEntrys;
	}
	
	public void setEmissionEntrys(List<EmissionEntryRESTModel> emissionEntrys) {
		this.emissionEntrys = emissionEntrys;
	}
}
