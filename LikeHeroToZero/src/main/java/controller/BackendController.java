package controller;

import java.io.Serializable;
import java.util.List;

import dao.EmissionEntryDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.EmissionEntry;

@Named
@ViewScoped
public class BackendController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private List<EmissionEntry> emissionList;
	
	@Inject
	private EmissionEntryDAO emissionDao;
	
	
	public BackendController()
	{
		
	}
	
	
	public void safe(int index)
	{
		emissionDao.updateEmissionEntry(emissionList.get(index));
	}
	
	
	public List<EmissionEntry> getEmissionList()
	{
		emissionList = emissionDao.findAll();
		
		return emissionList;
	}
}
