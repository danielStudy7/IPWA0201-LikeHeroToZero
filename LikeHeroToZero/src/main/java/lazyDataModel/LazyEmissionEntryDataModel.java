package lazyDataModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import dao.EmissionEntryDAO;
import jakarta.inject.Inject;
import model.EmissionEntry;

public class LazyEmissionEntryDataModel extends LazyDataModel<EmissionEntry> 
{
	private static final long serialVersionUID = 1L;
	
	private List<EmissionEntry> emissionList;
	
	@Inject
	private EmissionEntryDAO emissionDao;
	
	public LazyEmissionEntryDataModel()
	{
		emissionDao = new EmissionEntryDAO();
	}
	
	
	@Override
	public String getRowKey(EmissionEntry emissionEntry)
	{
		return String.valueOf(emissionEntry.getId());
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) 
	{
		Map<String, Object> filters = new HashMap<>();
		if (filterBy != null)
		{
			for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet())
			{	
				filters.put(entry.getKey(), entry.getValue().getFilterValue());
			}
		}
		
		return emissionDao.countEmissionEntrys(filters);
	}

	@Override
	public List<EmissionEntry> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) 
	{
		String sortField = null;
		SortOrder sortOrder = null;
		
		if (sortBy != null && !sortBy.isEmpty())
		{
			SortMeta sortMeta = sortBy.values().iterator().next();
			sortField = sortMeta.getField();
			sortOrder = sortMeta.getOrder();
		}
		
		Map<String, Object> filters = new HashMap<>();
		if (filterBy != null)
		{
			for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet())
			{	
				filters.put(entry.getKey(), entry.getValue().getFilterValue());
			}
		}
		
		emissionList = emissionDao.loadEmissionEntrys(first, pageSize, sortField, sortOrder, filters);
		
		return emissionList;
	}
}
