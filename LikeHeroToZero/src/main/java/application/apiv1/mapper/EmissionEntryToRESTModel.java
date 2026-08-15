package application.apiv1.mapper;

import application.apiv1.model.EmissionRESTModel;
import model.EmissionEntry;

public class EmissionEntryToRESTModel {
	public static EmissionRESTModel mapToRESTModel(EmissionEntry emissionEntry) {
		EmissionRESTModel result = new EmissionRESTModel();
		result.setId(emissionEntry.getId());
		result.setEmissions(emissionEntry.getEmissions());
		result.setChecked(emissionEntry.isChecked());
		result.setYear(emissionEntry.getYear());
		
		return result;
	}
}
