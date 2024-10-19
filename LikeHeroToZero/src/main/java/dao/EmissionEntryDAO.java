package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortOrder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import model.EmissionEntry;

@Named
@ApplicationScoped
public class EmissionEntryDAO 
{
	private EntityManager em;
	
	private CriteriaBuilder cb;
	
	
	public EmissionEntryDAO()
	{
		em = Persistence.createEntityManagerFactory("LikeHeroToZero").createEntityManager();
		cb = em.getCriteriaBuilder();
	}
	
	
	//Initial nach emission sortiert
	public List<EmissionEntry> findAll()
	{	
		List<EmissionEntry> emissionList;
		
		CriteriaQuery<EmissionEntry> cq = cb.createQuery(EmissionEntry.class);
		Root<EmissionEntry> emissionRoot = cq.from(EmissionEntry.class);
		
		cq.select(emissionRoot).orderBy(cb.desc(emissionRoot.get("year")));
		
		emissionList = em.createQuery(cq).getResultList();
		
		em.clear();
		
		return emissionList;
	}
	
	public void createEmissionEntry(EmissionEntry emissionEntry)
	{
		if (emissionEntry != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.persist(emissionEntry);
			t.commit();
			
			em.clear();			
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void deleteEmissionEntry(EmissionEntry emissionEntry)
	{
		if (emissionEntry != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.merge(emissionEntry);
				em.remove(emissionEntry);
			t.commit();
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void updateEmissionEntry(EmissionEntry emissionEntry)
	{
		if (emissionEntry != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.merge(emissionEntry);
			t.commit();
		}
		else
		{
			//TODO handeln
		}
	}
	
	public EmissionEntry getEmissionEntry(int index)
	{
		EmissionEntry emissionEntry;
		
		List<EmissionEntry> emissionList;
		
		CriteriaQuery<EmissionEntry> cq = cb.createQuery(EmissionEntry.class);
		cq.from(EmissionEntry.class);
		
		emissionList = em.createQuery(cq).getResultList();
		
		emissionEntry = emissionList.get(index);
		
		return emissionEntry;
	}
	
	
	//Methoden für das LazyEmissionEntryDataModel
	public int countEmissionEntrys(Map<String, FilterMeta> filters)
	{
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<EmissionEntry> emissionRoot = cq.from(EmissionEntry.class);
		
		//Anzahl abfragen
		cq.select(cb.count(emissionRoot));
		
		//Filtern
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (filters != null)
		{
			filters.forEach((k, v) ->
			{
				predicates.add(cb.like(cb.lower(emissionRoot.get(k)),  "%" + v.toString().toLowerCase() + "%"));
			});
		}
		
		cq.where(predicates.toArray(new Predicate[0]));
		
		int result = em.createQuery(cq).getSingleResult().intValue();
		
		em.clear();
		
		return result;
	}
	
	public List<EmissionEntry> loadEmissionEntrys(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filterBy)
	{
		CriteriaQuery<EmissionEntry> cq = cb.createQuery(EmissionEntry.class);
		Root<EmissionEntry> emissionRoot = cq.from(EmissionEntry.class);
		
		//Sortieren
		if (sortField != null)
		{
			if (sortOrder == SortOrder.ASCENDING)
			{
				cq.orderBy(cb.asc(emissionRoot.get(sortField)));
			}
			else if (sortOrder == SortOrder.DESCENDING)
			{
				cq.orderBy(cb.desc(emissionRoot.get(sortField)));
			}
		}
		
		//Filtern
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (filterBy != null)
		{
			filterBy.forEach((k,v) ->
			{
				predicates.add(cb.like(cb.lower(emissionRoot.get(k)), "%" + v.toString().toLowerCase() + "%"));
			});
		}
		
		cq.where(predicates.toArray(new Predicate[0]));
		
		//Abfrage ausführen
		List<EmissionEntry> resultList = em.createQuery(cq).setFirstResult(first).setMaxResults(pageSize).getResultList();
		
		em.clear();
		
		return resultList;
	}
}
