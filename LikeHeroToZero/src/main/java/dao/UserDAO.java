package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import model.User;

@Named
@ApplicationScoped
public class UserDAO extends AbstractDAO
{
	public UserDAO()
	{
		super();
	}
	
	public UserDAO(EntityManager entityManager) {
		super(entityManager);
	}
	
	public User getUserByUsername(String username)
	{
		
		CriteriaQuery<User> query = createQuery(User.class);
		
		Root<User> rootUser = query.from(User.class);
		Predicate usernameCondition = getCriteriaBuilder().equal(rootUser.get("userName"), username);
		query.select(rootUser).where(usernameCondition);

		User user = getEntityManager().createQuery(query).getSingleResult();
		
		getEntityManager().clear();
		
		return user;
	}
}
