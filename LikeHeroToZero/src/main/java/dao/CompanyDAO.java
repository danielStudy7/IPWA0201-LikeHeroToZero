package dao;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.Company;

@Named
@ApplicationScoped
public class CompanyDAO 
{
	private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("LikeHeroToZero");

	
	public List<Company> findAll()
	{
		List<Company> companyList;
		
		EntityManager em = emf.createEntityManager();
		TypedQuery<Company> q = em.createQuery("select a from Company a", Company.class);
		companyList = q.getResultList();
		
		em.close();
		
		return companyList;
	}
}
