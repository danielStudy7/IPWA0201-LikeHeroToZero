package dao;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
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
}
