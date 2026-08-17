package dao;

import java.util.List;
import java.util.UUID;

import common.FailedOperationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class AbstractDAO {
	
	private EntityManager entityManager;
	private CriteriaBuilder criteriaBuilder;
	
	public AbstractDAO() {
		this(Persistence.createEntityManagerFactory("LikeHeroToZero").createEntityManager());
	}
	
	public AbstractDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.criteriaBuilder = entityManager.getCriteriaBuilder();
	}
	
	public <T> T getEntity(UUID id, Class<T> clazz) {
		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		Predicate idCondition = criteriaBuilder.equal(root.get("id"), id);
		query.select(root).where(idCondition);
		
		T result = entityManager.createQuery(query).getSingleResult();
		
		entityManager.clear();
		return result;
	}
	
	public <T> List<T> getEntityList(Class<T> clazz) {
		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);
		query.from(clazz);
		
		List<T> resultList = entityManager.createQuery(query).getResultList();
		
		entityManager.clear();
		
		return resultList;
	}
	
	public <T> void createEntity(T entity) throws FailedOperationException {
		if (entity != null) {
			getTransaction().begin();
			entityManager.persist(entity);
			getTransaction().commit();
			
			entityManager.clear();
		}
		else {
			throw new FailedOperationException("Fehler beim Speichern des Objekts. Das Objekt ist null.");
		}
	}
	
	public <T> void deleteEntity(T entity) throws FailedOperationException {
		if (entity != null) {
			getTransaction().begin();
			entityManager.merge(entity);
			entityManager.remove(entity);
			getTransaction().commit();
			
			entityManager.clear();
		}
		else {
			throw new FailedOperationException("Fehler beim Löschen des Objekts. Das zu löschende Objekt ist null.");
		}
	}
	
	public <T> void updateEntity(T entity) throws FailedOperationException {
		if (entity != null) {
			getTransaction().begin();
			entityManager.merge(entity);
			getTransaction().commit();
			
			entityManager.clear();
		}
		else {
			throw new FailedOperationException("Fehler beim Update des Objekts. Das zu ändernde Objekt ist null.");
		}
	}
	
	public <T> CriteriaQuery<T> createQuery(Class<T> clazz) {
		return criteriaBuilder.createQuery(clazz);
	}
	
	public EntityManager getEntityManager() {
		return this.entityManager;
	} 
	
	public CriteriaBuilder getCriteriaBuilder() {
		return this.criteriaBuilder;
	}
	
	private EntityTransaction getTransaction() {
		return entityManager.getTransaction();
	}
}
