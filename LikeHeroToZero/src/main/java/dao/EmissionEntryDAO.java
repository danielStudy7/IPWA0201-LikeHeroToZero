package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import model.EmissionEntry;

@Named
@ApplicationScoped
public class EmissionEntryDAO extends AbstractDAO
{
	
	public EmissionEntryDAO()
	{
		super();
	}
	
	public EmissionEntryDAO(EntityManager entityManager) {
		super(entityManager);
	}
	
	public List<EmissionEntry> findAll()
	{	
		List<EmissionEntry> emissionList;
		
		CriteriaQuery<EmissionEntry> query = createQuery(EmissionEntry.class);
		Root<EmissionEntry> emissionRoot = query.from(EmissionEntry.class);
		query.select(emissionRoot).orderBy(getCriteriaBuilder().desc(emissionRoot.get("year")));
		
		emissionList = getEntityManager().createQuery(query).getResultList();
		
		getEntityManager().clear();
		
		return emissionList;
	}
	
	//Methoden für das LazyEmissionEntryDataModel
	public int countEmissionEntrys(Map<String, Object> filters)
	{
		CriteriaQuery<Long> query = createQuery(Long.class);
		Root<EmissionEntry> emissionRoot = query.from(EmissionEntry.class);
		query.select(getCriteriaBuilder().count(emissionRoot));
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (filters != null)
		{
			filters.forEach((k, v) ->
			{
				predicates.add(getCriteriaBuilder().equal(emissionRoot.get(k), v));
			});
		}
		
		query.where(predicates.toArray(new Predicate[0]));
		
		int result = getEntityManager().createQuery(query).getSingleResult().intValue();
		
		getEntityManager().clear();
		
		return result;
	}
	
	public List<EmissionEntry> loadEmissionEntrys(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filterBy)
	{
		CriteriaQuery<EmissionEntry> query = getCriteriaBuilder().createQuery(EmissionEntry.class);
		Root<EmissionEntry> emissionRoot = query.from(EmissionEntry.class);
		
		if (sortField != null)
		{
			if (sortOrder == SortOrder.ASCENDING)
			{
				query.orderBy(getCriteriaBuilder().asc(emissionRoot.get(sortField)));
			}
			else if (sortOrder == SortOrder.DESCENDING)
			{
				query.orderBy(getCriteriaBuilder().desc(emissionRoot.get(sortField)));
			}
		}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (filterBy != null)
		{
			filterBy.forEach((k,v) ->
			{		
				predicates.add(getCriteriaBuilder().equal(emissionRoot.get(k), v));
			});			
		}
		
		Predicate checkedPredicate = getCriteriaBuilder().equal(emissionRoot.get("checked"), true);
		predicates.add(checkedPredicate);
		
		query.where(predicates.toArray(new Predicate[0]));
		
		List<EmissionEntry> resultList = getEntityManager().createQuery(query).setFirstResult(first).setMaxResults(pageSize).getResultList();
		
		getEntityManager().clear();
		
		return resultList;
	}
}
