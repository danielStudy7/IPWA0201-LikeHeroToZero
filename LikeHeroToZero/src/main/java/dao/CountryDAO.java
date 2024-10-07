package dao;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.Country;

@Named
@ApplicationScoped
public class CountryDAO 
{
	private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("LikeHeroToZero");
	
	public List<Country> findAll()
	{
		List<Country> countryList;
		
		EntityManager em = emf.createEntityManager();
		TypedQuery<Country> q = em.createQuery("select a from Country a", Country.class);
		countryList = q.getResultList();
		
		em.close();
		
		System.out.println("CountryList aus DAO " + countryList.get(0).getContinentName());
		
		return countryList;
	}
}
