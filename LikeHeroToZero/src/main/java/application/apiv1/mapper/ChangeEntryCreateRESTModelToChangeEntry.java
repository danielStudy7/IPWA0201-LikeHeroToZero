package application.apiv1.mapper;

import model.ChangeEntry;
import model.ChangeEntryCreateRESTModel;

public class ChangeEntryCreateRESTModelToChangeEntry extends AbstractMapper<ChangeEntryCreateRESTModel, ChangeEntry> {

	@Override
	protected ChangeEntry mapInterval(ChangeEntryCreateRESTModel source) {

		ChangeEntry target = new ChangeEntry();
		target.setEmissions(source.getEmissions());
		target.setInfoText(source.getInfoText());
		target.setSource(source.getSource());
		target.setYear(source.getYear());
		target.setCountry(source.getCountry());
		
		return target;
	}

}
