package test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

abstract class SlowTestVorlage {

	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;
	
	@BeforeAll
	public static void initEmFactory() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory("test-database");
	}
	
    @AfterAll
    protected static void closeEmf() {
        if (entityManagerFactory != null) {
        	entityManagerFactory.close();
        }
        
        
    }

    @BeforeEach
    protected void initEm() {
    	entityManager = entityManagerFactory.createEntityManager();
    	getTransaction().begin();
    	createTestData();
    }

    @AfterEach
    protected void closeEm() {
        if (getTransaction().isActive()) {
        	getTransaction().rollback();
        }
        
        cleanUp();
        
        entityManager.close();
    }
    
    private void cleanUp() {
    	EntityTransaction transaction = getTransaction();
		transaction.begin();
    	entityManager.createQuery("DELETE FROM ChangeEntry").executeUpdate();
    	entityManager.createQuery("DELETE FROM EmissionEntry").executeUpdate();
    	entityManager.createQuery("DELETE FROM User").executeUpdate();
    	transaction.commit();
    }
    
    abstract void createTestData();

	public static EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public static EntityManager getEntityManager() {
		return entityManager;
	}
	
	public EntityTransaction getTransaction() {
		return entityManager.getTransaction();
	}
}
