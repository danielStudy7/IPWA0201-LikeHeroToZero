package application.apiv1.mapper;

import application.apiv1.model.EmissionEntryCreateRESTModel;
import model.EmissionEntry;

public class EmissionEntryCreateRESTModelToEmissionEntry extends AbstractMapper<EmissionEntryCreateRESTModel, EmissionEntry> {

	@Override
	protected EmissionEntry mapInterval(EmissionEntryCreateRESTModel source) {
		EmissionEntry target = new EmissionEntry();
		target.setEmissions(source.getEmissions());
		target.setChecked(source.isChecked());
		target.setYear(source.getYear());
		target.setCountry(source.getCountry());
		
		return target;
	}

}
