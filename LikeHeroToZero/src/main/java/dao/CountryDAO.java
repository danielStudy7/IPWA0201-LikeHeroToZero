package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.Country;

public class CountryDAO 
{
	private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("LikeHeroToZero");
	
	public List<Country> findAll()
	{
		EntityManager em = emf.createEntityManager();
		TypedQuery<Country> q = em.createQuery("select a from Country a", Country.class);
		
		List<Country> countryList = q.getResultList();
		
		return countryList;
	}
}
