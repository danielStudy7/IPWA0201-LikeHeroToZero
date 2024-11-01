package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.EmissionEntryDAO;
import dao.UserDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lazyDataModel.LazyEmissionEntryDataModel;
import model.EmissionEntry;
import model.User;

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
	
	
	public IndexController()
	{
		lazyDataModel = new LazyEmissionEntryDataModel();
		emissionEntryDao = new EmissionEntryDAO();
		
		if (emissionEntryDao.findAll().isEmpty() || emissionEntryDao.findAll() == null)
		{
			userDao = new UserDAO();
			User systemUser;
			
			if (userDao.getUser("system") == null)
			{
				systemUser = new User("system", "system");
				userDao.createUser(systemUser);				
			}
			else
			{
				systemUser = userDao.getUser("system");
			}
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			try
			{
				InputStream inputData = IndexController.class.getClassLoader().getResourceAsStream("data.json");
				
				if (inputData == null)
				{
					throw new IOException("Die Datei \"data.json\" konnte nicht gefunden werden.");
				}
				
				List<EmissionEntry> emissionEntryList = objectMapper.readValue(inputData, new TypeReference<List<EmissionEntry>>() {});
				
				for (EmissionEntry emissionEntry : emissionEntryList)
				{
					emissionEntry.setUser(systemUser);
					emissionEntry.setChecked(true);
					emissionEntryDao.createEmissionEntry(emissionEntry);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			//nix
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
