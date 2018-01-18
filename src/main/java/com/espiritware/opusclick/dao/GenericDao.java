package com.espiritware.opusclick.dao;

import java.util.List;

public interface GenericDao<T> {
	
	public void setEntityClass( Class<T> entityClass );
	
	T findById(final String id);
	
	List<T> findAll();
	
	void create(final T entity);
	 
	void update(final T entity);
	 
	void delete(final T entity);
	 
	void deleteById(final String entityId);
}
