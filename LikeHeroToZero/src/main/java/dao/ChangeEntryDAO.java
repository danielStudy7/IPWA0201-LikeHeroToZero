package dao;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import model.ChangeEntry;
import model.User;

@Named
@ApplicationScoped
public class ChangeEntryDAO extends AbstractDAO
{
	
	public ChangeEntryDAO()
	{
		super();
	}
	
	public ChangeEntryDAO(EntityManager entityManager) {
		super(entityManager);
	}
	
	public List<ChangeEntry> getChangeListByUser(User currentUser)
	{
		List<ChangeEntry> changeEntryList;
		CriteriaQuery<ChangeEntry> query = createQuery(ChangeEntry.class);
		Root<ChangeEntry> changeEntryRoot = query.from(ChangeEntry.class);
		
		Predicate userPredicate = getCriteriaBuilder().equal(changeEntryRoot.get("createUser"), currentUser);
		
		Predicate checkedPredicate = getCriteriaBuilder().isFalse(changeEntryRoot.get("accepted"));
		Predicate declinedPredicate = getCriteriaBuilder().isFalse(changeEntryRoot.get("declined"));
		Predicate finalPredicate = getCriteriaBuilder().and(checkedPredicate, declinedPredicate);		
		
		query.where(getCriteriaBuilder().and(userPredicate, finalPredicate));
		
		changeEntryList = getEntityManager().createQuery(query).getResultList();
		
		getEntityManager().clear();
		
		return changeEntryList;
	}
	
}