package controller;

import java.io.Serializable;
import java.util.List;

import org.primefaces.event.SelectEvent;

import dao.EmissionEntryDAO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.ChangeEntry;
import model.EmissionEntry;

@Named
@ViewScoped
public class BackendController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private List<EmissionEntry> emissionList;
	private EmissionEntry emissionEntry;
	private EmissionEntry selectedEmissionEntry;
	private ChangeEntry changeEntry;
	
	@Inject
	private EmissionEntryDAO emissionDao;
	
	private int index = 0;

	
	public BackendController()
	{
		changeEntry = new ChangeEntry();
	}
	
	
	public void onRowSelect(SelectEvent<EmissionEntry> event)
	{
		selectedEmissionEntry = event.getObject();
	}
	
	public void createChangeEntry()
	{
		System.out.println("createChangeEntry() wird aufgerufen!");
	}
	
	
	public void edit()
	{
		index = emissionList.indexOf(selectedEmissionEntry);
		
		System.out.println("Index ist: " + index);
	}
	
	
	public List<EmissionEntry> getEmissionList()
	{
		emissionList = emissionDao.findAll();
		
		return emissionList;
	}
	
	public EmissionEntry getEmissionEntry()
	{
		return emissionEntry;
	}
	
	public EmissionEntry getSelectedEmissionEntry()
	{
		return selectedEmissionEntry;
	}
	
	public ChangeEntry getChangeEntry()
	{
		return changeEntry;
	}
	
	
	public void setSelectedEmissionEntry(EmissionEntry selectedEmissionEntry)
	{
		this.selectedEmissionEntry = selectedEmissionEntry;
	}
}
