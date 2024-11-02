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
	
	
	//Konstruktor
	public ChangeEntryDAO()
	{
		em = Persistence.createEntityManagerFactory("LikeHeroToZero").createEntityManager();
		cb = em.getCriteriaBuilder();
	}
	
	
	//Datenbank abfragen
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
			//Keine weitere Aktion notwendig
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
			//Keine weitere Aktion notwendig
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
			//Keine weitere Aktion notwendig
		}
	}

	
	//Akzeptieren und Ablehnen von Änderungen
	public void acceptChangeEntry(ChangeEntry changeEntry)
	{
		if (changeEntry != null)
		{
			changeEntry.setAccepted(true);
			
			if (changeEntry.getEmissionEntry() != null)
			{
				changeEntry.getEmissionEntry().setChecked(true);
				
				EntityTransaction t = em.getTransaction();
				
				t.begin();
				em.merge(changeEntry);
				t.commit();	
				
				em.clear();
			}
			else 
			{
				
			}
		}
		else
		{
			//Keine weitere Aktion notwendig
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
			//Keine weitere Aktion notwendig
		}
	}
	
	//Abfrage der Liste an Änderungen für Einträge des aktuellen User
	//Gibt ChangeEntrys zurück, die nicht accepted oder declined sind
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
		
		em.clear();
		
		return changeEntryList;
	}
}