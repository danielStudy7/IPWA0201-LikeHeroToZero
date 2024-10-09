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
import model.User;

@Named
@ApplicationScoped
public class UserDAO 
{
	private EntityManager em;
	
	private CriteriaBuilder cb;
	
	
	public UserDAO()
	{
		em = Persistence.createEntityManagerFactory("LikeHeroToZero").createEntityManager();
		cb = em.getCriteriaBuilder();
	}
	
	
	public List<User> getUserList()
	{
		List<User> userList;
		
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		cq.from(User.class);
		
		userList = em.createQuery(cq).getResultList();
		
		return userList;
	}
	
	public User getUser(String username)
	{
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		
		Root<User> rootUser = cq.from(User.class);
		Predicate usernameCondition = cb.equal(rootUser.get("username"), username);
		cq.select(rootUser).where(usernameCondition);
		
		return em.createQuery(cq).getSingleResult();
	}
	
	public void createUser(User user)
	{
		if (user != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
				em.persist(user);
			t.commit();			
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void updateUser(User user)
	{
		if (user != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
			em.merge(user);
			t.commit();			
		}
		else
		{
			//TODO handeln
		}
	}
	
	public void deleteUser(User user)
	{
		if (user != null)
		{
			EntityTransaction t = em.getTransaction();
			
			t.begin();
			em.merge(user);
			em.remove(user);
			t.commit();			
		}
		else
		{
			//TODO handeln
		}
	}
}
