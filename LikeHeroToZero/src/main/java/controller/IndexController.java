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
	
	private LazyEmissionEntryDataModel lazyDataModel;

	private List<String> selectedItems;
	
	
	public IndexController()
	{
		lazyDataModel = new LazyEmissionEntryDataModel();
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
