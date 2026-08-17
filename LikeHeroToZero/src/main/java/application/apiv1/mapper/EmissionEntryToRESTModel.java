package application.apiv1.mapper;

import application.apiv1.model.EmissionEntryRESTModel;
import model.EmissionEntry;

public class EmissionEntryToRESTModel extends AbstractMapper<EmissionEntry, EmissionEntryRESTModel> {
	
	@Override
	protected EmissionEntryRESTModel mapInterval(EmissionEntry source) {
	
		EmissionEntryRESTModel result = new EmissionEntryRESTModel();
		result.setId(source.getId());
		result.setEmissions(source.getEmissions());
		result.setChecked(source.isChecked());
		result.setYear(source.getYear());
		
		return result;
	}
}
