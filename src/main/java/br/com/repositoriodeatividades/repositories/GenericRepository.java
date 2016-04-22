package br.com.repositoriodeatividades.repositories;

import br.com.repositoriodeatividades.usecases.interfaces.GenericRepositoryInterface;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
class GenericRepositoryImplementation<T> implements GenericRepositoryInterface<T> {

    protected EntityManager entityManager;
    private Class<T> type;

    public GenericRepositoryImplementation() {
    }

    public GenericRepositoryImplementation(Class<T> type) {
        this.type = type;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public T save(T emp) {
        entityManager.persist(emp);
        entityManager.flush();
        return emp;
    }

    @Override
    public Boolean delete(T emp) {
        try {
            entityManager.remove(emp);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public T edit(T emp) {
        try{
            return entityManager.merge(emp);
        } catch(Exception ex) {
            return null;
        }
    }

    @Override
    public T find(Long empId) {
        return (T) entityManager.find(type.getClass(), empId);
    }
}