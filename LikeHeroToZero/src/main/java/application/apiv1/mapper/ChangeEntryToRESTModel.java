package application.apiv1.mapper;

import model.ChangeEntry;
import model.ChangeEntryRESTModel;

public class ChangeEntryToRESTModel extends AbstractMapper<ChangeEntry, ChangeEntryRESTModel>{

	@Override
	protected ChangeEntryRESTModel mapInterval(ChangeEntry source) {

		ChangeEntryRESTModel target = new ChangeEntryRESTModel();
		target.setUuid(source.getId().toString());
		target.setAccepted(source.isAccepted());
		target.setDeclined(source.isDeclined());
		target.setEmissions(source.getEmissions());
		target.setInfoText(source.getInfoText());
		target.setSource(source.getSource());
		target.setYear(source.getYear());
		target.setEmissionEntryUUID(source.getEmissionEntry().getId().toString());
		target.setCountry(source.getCountry());
		
		return target;
	}

}
