package com.espiritware.opusclick.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractHibernateDao<T> {

	@Autowired
	private SessionFactory sessionFactory;

	private Class<T> entityClass;
	
	public void setEntityClass( Class<T> entityClass ){
	      this.entityClass = entityClass;
	}
	 
	   public T findById( String id ){
	      return (T) getCurrentSession().get( entityClass, id );
	   }
	   public List<T> findAll(){
	      return getCurrentSession().createQuery( "from " + entityClass.getName() ).list();
	   }
	 
	   public void create( T entity ){
	      getCurrentSession().persist( entity );
	   }
	 
	   public void update( T entity ){
	      getCurrentSession().merge( entity );
	   }
	 
	   public void delete( T entity ){
	      getCurrentSession().delete( entity );
	   }
	   public void deleteById( String entityId ) {
	      T entity = findById( entityId );
	      delete( entity );
	   }
	 
	   protected Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	
}
