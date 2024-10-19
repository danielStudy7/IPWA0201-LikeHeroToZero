package controller;

import java.io.Serializable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lazyDataModel.LazyEmissionEntryDataModel;

@Named
@ApplicationScoped
public class IndexController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private LazyEmissionEntryDataModel lazyDataModel;
	
	
	public IndexController()
	{
		lazyDataModel = new LazyEmissionEntryDataModel();
	}
	
	
	public LazyEmissionEntryDataModel getLazyDataModel()
	{	
		return lazyDataModel;
	}
}
