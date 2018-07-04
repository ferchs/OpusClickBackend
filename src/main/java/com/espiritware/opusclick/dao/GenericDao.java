package com.espiritware.opusclick.dao;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;

public interface GenericDao<T,E extends Serializable> {
	
	public void setEntityClass( Class<T> entityClass );
	
	T findById(final E id);
	
	List<T> findAll();
	
	T create(final T entity);
	 
	T update(final T entity);
	 
	void delete(final T entity);
	 
	void deleteById(final E entityId);
	
	T findByField(String fieldName, String fieldValue);
	
	List<T> findAllByField(String fieldName, String fieldValue);
	
	List<T> findAllByField(String fieldName, String [] fieldValues);
	
	Session getCurrentSession();
	
}
