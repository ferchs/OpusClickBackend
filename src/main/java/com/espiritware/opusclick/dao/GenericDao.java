package com.espiritware.opusclick.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T,E extends Serializable> {
	
	public void setEntityClass( Class<T> entityClass );
	
	T findById(final E id);
	
	List<T> findAll();
	
	void create(final T entity);
	 
	void update(final T entity);
	 
	void delete(final T entity);
	 
	void deleteById(final E entityId);
	
	T findByUniquefield(String fieldName, String fieldValue);
	
	List<T> findByfield(String fieldName, String fieldValue);
	
	T getSessionFactory();
	
}
