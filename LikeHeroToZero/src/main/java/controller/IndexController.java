package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.FailedOperationException;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lazyDataModel.LazyEmissionEntryDataModel;
import model.Country;
import model.EmissionEntry;
import model.User;
import service.EmissionEntryService;
import service.UserService;

@Named
@ViewScoped
public class IndexController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private LazyEmissionEntryDataModel lazyDataModel;

	private List<String> selectedItems;
	
	@Inject
	private EmissionEntryService emissionEntryService;
	
	@Inject
	private UserService userService;
	
	private List<EmissionEntry> emissionEntries;
	
	public IndexController() throws FailedOperationException
	{
		// CDI
	}
	
	@PostConstruct
	public void init() throws FailedOperationException {
		emissionEntries = emissionEntryService.findAll();

		lazyDataModel = new LazyEmissionEntryDataModel();
		
		if (emissionEntries.isEmpty() || emissionEntries == null)
		{
			User systemUser = getOrCreateSystemUser();
			
			importInitinalData(systemUser);
		}
	}

	private void importInitinalData(User systemUser) throws FailedOperationException {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try
		{
		    InputStream inputData = IndexController.class.getClassLoader().getResourceAsStream("data.json");

		    if (inputData == null)
		    {
		        throw new IOException("Die Datei \"data.json\" konnte nicht gefunden werden.");
		    }

		    JsonNode rootNode = objectMapper.readTree(inputData);

		    for (JsonNode node : rootNode)
		    {
		        EmissionEntry emissionEntry = new EmissionEntry();
		        emissionEntry.setEmissions(node.findValue("emissions").asDouble());
		        emissionEntry.setYear(node.findValue("year").asInt());
		        emissionEntry.setChecked(true);
		        emissionEntry.setCountry(Country.fromDisplayName(node.findValue("country").asText()));
		        emissionEntryService.createEmissionEntry(emissionEntry, systemUser);
		    }
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private User getOrCreateSystemUser() throws FailedOperationException {
		User systemUser;
		
		if (userService.getUserByUsername("system") == null)
		{
			systemUser = new User("system", "system");
			userService.createUser(systemUser);
		}
		else
		{
			systemUser = userService.getUserByUsername("system");
		}
		return systemUser;
	}
	
	public LazyEmissionEntryDataModel getLazyDataModel()
	{	
		return lazyDataModel;
	}

	public List<String> getSelectedItems()
	{
		return selectedItems;
	}
	
	public void setSelectedItems(List<String> selectedItems)
	{
		this.selectedItems = selectedItems;
	}
}
