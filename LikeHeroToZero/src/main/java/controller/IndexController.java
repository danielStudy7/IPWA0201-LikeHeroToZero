package controller;

import java.io.Serializable;
import java.util.List;

import org.jboss.weld.security.NewInstanceAction;

import dao.EmissionEntryDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lazyDataModel.LazyEmissionEntryDataModel;
import model.EmissionEntry;

@Named
@ApplicationScoped
public class IndexController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private LazyEmissionEntryDataModel lazyDataModel;
	
	//@Inject
	//private EmissionEntryDAO emissionDao;
	
	
	public IndexController()
	{
		lazyDataModel = new LazyEmissionEntryDataModel();
	}
	
	
	public LazyEmissionEntryDataModel getLazyDataModel()
	{	
		return lazyDataModel;
	}
}
