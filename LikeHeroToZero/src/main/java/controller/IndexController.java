package controller;

import java.io.Serializable;
import java.util.List;

import dao.EmissionEntryDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lazyDataModel.LazyEmissionEntryDataModel;
import model.EmissionEntry;

@Named
@SessionScoped
public class IndexController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmissionEntryDAO emissionDao;
	
	private LazyEmissionEntryDataModel lazyDataModel;
	
	private List<String> countryList;
	private List<String> selectedItems;
	
	
	public IndexController()
	{
		lazyDataModel = new LazyEmissionEntryDataModel();
	}
	
	
	public List<EmissionEntry> updateTable()
	{
		return lazyDataModel.loadDataByCountry(selectedItems);
	}
	
	
	public LazyEmissionEntryDataModel getLazyDataModel()
	{	
		return lazyDataModel;
	}
	
	public List<String> getCountryList()
	{
		countryList = emissionDao.getCountriesFromData();
		
		return countryList;
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
