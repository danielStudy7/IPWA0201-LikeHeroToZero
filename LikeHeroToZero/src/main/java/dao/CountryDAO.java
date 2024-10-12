package dao;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import model.Country;

@Named
@ApplicationScoped
public class CountryDAO 
{
	private EntityManager em;
	
	private CriteriaBuilder cb;
	
	
	public CountryDAO()
	{
		em = Persistence.createEntityManagerFactory("LikeHeroToZero").createEntityManager();
		cb = em.getCriteriaBuilder();
	}
	
	
	public List<Country> findAll()
	{	
		List<Country> countryList;
		
		CriteriaQuery<Country> cq = cb.createQuery(Country.class);
		cq.from(Country.class);
		
		countryList = em.createQuery(cq).getResultList();
		
		em.clear();
		
		return countryList;
	}
	
	public void createCountryEntry(Country country)
	{
		if (country != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.persist(country);
			t.commit();
			
			em.clear();			
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void deleteCountryEntry(Country country)
	{
		if (country != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.merge(country);
				em.remove(country);
			t.commit();
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void updateCountryEntry(Country country)
	{
		if (country != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.merge(country);
			t.commit();
		}
		else
		{
			//TODO handeln
		}
	}
	
	public Country getCountryEntry(int index)
	{
		Country country;
		
		List<Country> countryList;
		
		CriteriaQuery<Country> cq = cb.createQuery(Country.class);
		cq.from(Country.class);
		
		countryList = em.createQuery(cq).getResultList();
		
		country = countryList.get(index);
		
		return country;
	}
}
