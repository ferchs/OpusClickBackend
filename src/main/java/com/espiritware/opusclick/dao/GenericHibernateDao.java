package com.espiritware.opusclick.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class GenericHibernateDao<T> extends AbstractHibernateDao<T> implements GenericDao<T>{	
}
