package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.FailedOperationException;
import dao.EmissionEntryDAO;
import dao.UserDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lazyDataModel.LazyEmissionEntryDataModel;
import model.Country;
import model.EmissionEntry;
import model.User;
import service.UserService;

@Named
@ViewScoped
public class IndexController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private LazyEmissionEntryDataModel lazyDataModel;

	private List<String> selectedItems;
	
	@Inject
	private EmissionEntryDAO emissionEntryDao;
	
	@Inject
	private UserDAO userDao;
	
	@Inject
	private UserService userService;
	
	
	//Konstruktor
	//Erstellt initiale Standarddaten, wenn keine vorhanden sind 
	//Erstellt einen Standard-User für die Standarddaten
	public IndexController() throws FailedOperationException
	{
		// TODO Refactoring IndexService
		lazyDataModel = new LazyEmissionEntryDataModel();
		emissionEntryDao = new EmissionEntryDAO();
		
		if (emissionEntryDao.findAll().isEmpty() || emissionEntryDao.findAll() == null)
		{
			userDao = new UserDAO();
			User systemUser;
			
			// FIXME getSingleResult gibt nicht mehr null zurück wenn nichts gefunden wird 
			if (userDao.getUserByUsername("system") == null)
			{
				systemUser = new User("system", "system");
				userService.createUser(systemUser);
			}
			else
			{
				systemUser = userDao.getUserByUsername("system");
			}
			
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
			        emissionEntry.setUser(systemUser);
			        emissionEntry.setChecked(true);
			        emissionEntry.setCountry(Country.fromDisplayName(node.findValue("country").asText()));
			        emissionEntryDao.createEntity(emissionEntry);
			    }
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
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
