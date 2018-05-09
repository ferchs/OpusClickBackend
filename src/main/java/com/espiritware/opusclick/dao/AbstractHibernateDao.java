package com.espiritware.opusclick.dao;

import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractHibernateDao<T,E extends Serializable> {

	@Autowired
	private SessionFactory sessionFactory;

	private Class<T> entityClass;

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	@Transactional
	public T findById(E id) {
		return (T) getCurrentSession().get(entityClass, id);
	}
	
	@Transactional
	public List<T> findAll() {
		return getCurrentSession().createQuery("from " + entityClass.getName()).list();
	}
	
	@Transactional
	public void create(T entity) {
		getCurrentSession().persist(entity);
	}
	
	@Transactional
	public void update(T entity) {
		getCurrentSession().merge(entity);
	}
	
	@Transactional
	public void delete(T entity) {
		getCurrentSession().delete(entity);
	}
	
	@Transactional
	public void deleteById(E entityId) {
		T entity = findById(entityId);
		delete(entity);
	}
	
	@Transactional
	public T findByUniquefield(String fieldName, String fieldValue) {
		String entity= entityClass.getName().substring(entityClass.getName().lastIndexOf(".")+1,entityClass.getName().length());
		return (T) getCurrentSession()
				.createQuery("from " + entity + " where " + fieldName + " = :fieldValue")
				.setParameter("fieldValue", fieldValue).uniqueResult();
	}
	
	@Transactional
	public List<T> findByfield(String fieldName, String fieldValue) {
		String entity= entityClass.getName().substring(entityClass.getName().lastIndexOf(".")+1,entityClass.getName().length());
		return getCurrentSession()
				.createQuery("from " + entity + " where " + fieldName + " = :fieldValue")
				.setParameter("fieldValue", fieldValue).list();
	}
	
	public T getSessionFactory() {
		return (T) sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
}
