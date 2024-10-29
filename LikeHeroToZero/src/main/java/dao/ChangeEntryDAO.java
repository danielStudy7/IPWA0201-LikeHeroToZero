package dao;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import model.ChangeEntry;
import model.User;

@Named
@ApplicationScoped
public class ChangeEntryDAO 
{
	private EntityManager em;
	
	private CriteriaBuilder cb;
	
	
	public ChangeEntryDAO()
	{
		em = Persistence.createEntityManagerFactory("LikeHeroToZero").createEntityManager();
		cb = em.getCriteriaBuilder();
	}
	
	
	public void createChangeEntry(ChangeEntry changeEntry)
	{
		if (changeEntry != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.persist(changeEntry);
			t.commit();
			
			em.clear();
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void deleteChangeEntry(ChangeEntry changeEntry)
	{
		if (changeEntry != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.merge(changeEntry);
				em.remove(changeEntry);
			t.commit();
			
			em.clear();
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void updateChangeEntry(ChangeEntry changeEntry)
	{
		if (changeEntry != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.merge(changeEntry);
			t.commit();
			
			em.clear();
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void getChangeEntry()
	{
		//TODO Nach id abfragen
	}
	
	
	public void acceptChangeEntry(ChangeEntry changeEntry)
	{
		if (changeEntry != null)
		{
			changeEntry.setAccepted(true);
			changeEntry.getEmissionEntry().setChecked(true);
			
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.merge(changeEntry);
			t.commit();	
			
			em.clear();
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void declineChangeEntry(ChangeEntry changeEntry)
	{
		if (changeEntry != null)
		{
			changeEntry.setDeclined(true);
			
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.merge(changeEntry);
			t.commit();
			
			em.clear();
		}
		else
		{
			//TODO handeln
		}
	}
	
	public List<ChangeEntry> getChangeList(User currentUser)
	{
		List<ChangeEntry> changeEntryList;
		CriteriaQuery<ChangeEntry> cq = cb.createQuery(ChangeEntry.class);
		Root<ChangeEntry> changeEntryRoot = cq.from(ChangeEntry.class);
		
		Predicate userPredicate = cb.equal(changeEntryRoot.get("createUser"), currentUser);
		
		Predicate checkedPredicate = cb.isFalse(changeEntryRoot.get("accepted"));
		Predicate declinedPredicate = cb.isFalse(changeEntryRoot.get("declined"));
		Predicate finalPredicate = cb.and(checkedPredicate, declinedPredicate);		
		
		cq.where(cb.and(userPredicate, finalPredicate));
		
		changeEntryList = em.createQuery(cq).getResultList();
		
		//System.out.println("Änderungsanträge: " + changeEntryList.get(0).getCountry() + " " + changeEntryList.get(0));
		
		return changeEntryList;
	}
}