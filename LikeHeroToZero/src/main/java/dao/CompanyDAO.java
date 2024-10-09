package dao;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import model.Company;

@Named
@ApplicationScoped
public class CompanyDAO 
{
	private EntityManager em;
	
	private CriteriaBuilder cb;
	
	
	public CompanyDAO()
	{
		em = Persistence.createEntityManagerFactory("LikeHeroToZero").createEntityManager();
		cb = em.getCriteriaBuilder();
	}
	
	
	public List<Company> findAll()
	{
		List<Company> companyList;
		
		CriteriaQuery<Company> cq = cb.createQuery(Company.class);
		cq.from(Company.class);

		companyList = em.createQuery(cq).getResultList();
		
		return companyList;
	}
		
	public void safeCompanyEntrie(Company company)
	{
		if (company != null)
		{
			EntityTransaction t = em.getTransaction();

			t.begin();
				em.persist(company);
			t.commit();			
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void deleteCompanyEntrie(Company company)
	{
		if (company != null)
		{
			EntityTransaction t = em.getTransaction();

			t.begin();
			em.merge(company);
			em.remove(company);
			t.commit();			
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void updateCompany(Company company)
	{		
		if (company != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
			em.merge(company);
			t.commit();			
		}
	}
	
	public Company getCompanyEntrie(int index)
	{
		Company company;
		List<Company> companyList;
		
		CriteriaQuery<Company> cq = cb.createQuery(Company.class);
		cq.from(Company.class);
		companyList = em.createQuery(cq).getResultList();
		
		company = companyList.get(index);
		
		return company;
	}
}
