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
	public T create(T entity) {
		getCurrentSession().persist(entity);
		return entity;
	}
	
	@Transactional
	public T update(T entity) {
		getCurrentSession().merge(entity);
		return entity;
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
	public T findByField(String fieldName, String fieldValue) {
		String entity= entityClass.getName().substring(entityClass.getName().lastIndexOf(".")+1,entityClass.getName().length());
		return (T) getCurrentSession()
				.createQuery("from " + entity + " where " + fieldName + " = :fieldValue")
				.setParameter("fieldValue", fieldValue).uniqueResult();
	}
	
	@Transactional
	public List<T> findAllByField(String fieldName, String fieldValue) {
		String entity= entityClass.getName().substring(entityClass.getName().lastIndexOf(".")+1,entityClass.getName().length());
		return getCurrentSession()
				.createQuery("from " + entity + " where " + fieldName + " = :fieldValue")
				.setParameter("fieldValue", fieldValue).list();
	}
	
	@Transactional
	public List<T> findAllByField(String fieldName, String[] fieldValues) {
		String entity= entityClass.getName().substring(entityClass.getName().lastIndexOf(".")+1,entityClass.getName().length());
		return getCurrentSession()
				.createQuery("from " + entity + " where " + fieldName + " in (:fieldValues)")
				.setParameterList("fieldValues", fieldValues).list();
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
}
