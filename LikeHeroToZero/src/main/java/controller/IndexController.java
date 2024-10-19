package controller;

import java.io.Serializable;
import java.util.List;

import dao.EmissionEntryDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.EmissionEntry;

@Named
@ApplicationScoped
public class IndexController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<EmissionEntry> emissionList;
	
	@Inject
	private EmissionEntryDAO emissionDao;
	
	
	public IndexController()
	{

	}
	
	
	public List<EmissionEntry> getEmissionList()
	{
		emissionList = emissionDao.findAll();
		
		return emissionList;
	}
}
