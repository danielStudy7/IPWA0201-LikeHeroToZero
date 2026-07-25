package lazyDataModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
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
	
	
	//Konstruktor
	public LazyEmissionEntryDataModel()
	{
		emissionDao = new EmissionEntryDAO();
	}
	
	
	//Überschriebene Methoden
	//Relevant für die Nutzung des LazyDataModels
	@Override
	public String getRowKey(EmissionEntry emissionEntry)
	{
		return String.valueOf(emissionEntry.getId());
	}
	
	@Override
	public EmissionEntry getRowData(String rowKey)
	{
		int id = Integer.valueOf(rowKey);
		return  emissionDao.getEntity(id, EmissionEntry.class);
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
		Pair<String,SortOrder> sortOrder = createSortOrder(sortBy);
		
		Map<String, Object> filters = createFilterHashMap(filterBy);
		emissionList = emissionDao.loadEmissionEntrys(first, pageSize, sortOrder.getLeft(), sortOrder.getRight(), filters);
		
		return emissionList;
	}

	private Pair<String, SortOrder> createSortOrder(Map<String, SortMeta> sortBy) {
		
		String sortField = null;
		SortOrder sortOrder = null;
		
		if (sortBy != null && !sortBy.isEmpty())
		{
			SortMeta sortMeta = sortBy.values().iterator().next();
			sortField = sortMeta.getField();
			sortOrder = sortMeta.getOrder();
		}
		
		return Pair.of(sortField, sortOrder);
	}

	private Map<String, Object> createFilterHashMap(Map<String, FilterMeta> filterBy) {
		Map<String, Object> filters = new HashMap<>();
		
		if (filterBy != null)
		{
			for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet())
			{	
				filters.put(entry.getKey(), entry.getValue().getFilterValue());
			}
		}
		return filters;
	}
	
	
	//Getter reduziert auf <code>getEmissionList()</code>, weil als einziger relevant für JSF Zugriff
	public List<EmissionEntry> getEmissionList()
	{
		return emissionList;
	}
}
