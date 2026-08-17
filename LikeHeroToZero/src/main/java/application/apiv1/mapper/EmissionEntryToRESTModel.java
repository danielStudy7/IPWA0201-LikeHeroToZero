package application.apiv1.mapper;

import application.apiv1.model.EmissionEntryRESTModel;
import model.EmissionEntry;

public class EmissionEntryToRESTModel extends AbstractMapper<EmissionEntry, EmissionEntryRESTModel> {
	
	@Override
	protected EmissionEntryRESTModel mapInterval(EmissionEntry source) {
	
		EmissionEntryRESTModel target = new EmissionEntryRESTModel();
		target.setId(source.getId().toString());
		target.setEmissions(source.getEmissions());
		target.setChecked(source.isChecked());
		target.setYear(source.getYear());
		target.setCountry(source.getCountry());
		
		return target;
	}
}
